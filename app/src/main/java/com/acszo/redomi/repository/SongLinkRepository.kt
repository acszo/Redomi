package com.acszo.redomi.repository

import com.acszo.redomi.model.DeezerMetadata
import com.acszo.redomi.model.Link
import com.acszo.redomi.model.Providers
import com.acszo.redomi.model.QobuzEntity
import com.acszo.redomi.model.QobuzLink
import com.acszo.redomi.model.Song
import com.acszo.redomi.service.ApiResult
import com.acszo.redomi.service.DeezerService
import com.acszo.redomi.service.SongLinkService
import com.acszo.redomi.service.apiCall
import com.acszo.redomi.service.key
import com.acszo.redomi.utils.QobuzMatcher
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class SongLinkRepository(
    private val songLinkService: SongLinkService,
    private val qobuzRepository: QobuzRepository,
    private val deezerService: DeezerService
) {

    suspend fun getSongs(
        url: String,
        countryCode: String,
        includeQobuz: Boolean = true
    ): ApiResult<Providers> {
        if (url.toHttpUrlOrNull() == null) return ApiResult.Error(400)
        if (QobuzLink.isQobuzUrl(url)) {
            val link = QobuzLink.parse(url) ?: return ApiResult.Error(400)
            return resolveQobuz(link, countryCode)
        }
        val response = apiCall { songLinkService.getSongs(url, key(), countryCode) }
        if (response !is ApiResult.Success) return response
        val data = response.data
        val source = data.entitiesByUniqueId[data.entityUniqueId] ?: return ApiResult.Error(502)
        if (!includeQobuz || data.linksByPlatform.containsKey("qobuz")) return response
        if (source.type !in setOf("song", "album") ||
            source.title.isNullOrBlank() || source.artistName.isNullOrBlank()) {
            return ApiResult.Success(data.copy(lookupFailed = true))
        }

        val identifier = lookupIdentifier(source, data.linksByPlatform["deezer"]?.url)
        val knownIdentifier = when (identifier) {
            is ApiResult.Success -> identifier.data
            else -> null
        }
        return when (val match = qobuzRepository.findMatch(source, knownIdentifier)) {
            is ApiResult.Success -> ApiResult.Success(
                match.data?.let { addQobuz(data, it) }
                    ?: data.copy(lookupFailed = identifier !is ApiResult.Success)
            )
            is ApiResult.Error, is ApiResult.Exception -> ApiResult.Success(data.copy(lookupFailed = true))
        }
    }

    private suspend fun resolveQobuz(link: QobuzLink, countryCode: String): ApiResult<Providers> {
        val source = when (val response = qobuzRepository.getEntity(link)) {
            is ApiResult.Success -> response.data
            is ApiResult.Error -> return response
            is ApiResult.Exception -> return response
        }
        val sourceOnly = source.toProviders()
        return when (val bridge = bridgeUrl(source)) {
            is ApiResult.Success -> {
                val url = bridge.data ?: return ApiResult.Success(sourceOnly)
                when (val converted = apiCall { songLinkService.getSongs(url, key(), countryCode) }) {
                    is ApiResult.Success -> {
                        if (converted.data.entitiesByUniqueId[converted.data.entityUniqueId]?.type != link.type.songType) {
                            ApiResult.Success(sourceOnly.copy(lookupFailed = true))
                        } else {
                            ApiResult.Success(addQobuz(converted.data, source)
                                .copy(entityUniqueId = link.entityUniqueId))
                        }
                    }
                    is ApiResult.Error, is ApiResult.Exception ->
                        ApiResult.Success(sourceOnly.copy(lookupFailed = true))
                }
            }
            is ApiResult.Error, is ApiResult.Exception ->
                ApiResult.Success(sourceOnly.copy(lookupFailed = true))
        }
    }

    private fun addQobuz(data: Providers, entity: QobuzEntity): Providers = data.copy(
        linksByPlatform = data.linksByPlatform +
            ("qobuz" to Link(entity.link.url, entity.link.entityUniqueId)),
        entitiesByUniqueId = data.entitiesByUniqueId + (entity.link.entityUniqueId to entity.toSong())
    )

    private suspend fun bridgeUrl(source: QobuzEntity): ApiResult<String?> {
        val isTrack = source.link.type == QobuzLink.Type.TRACK
        val identifier = (if (isTrack) source.isrc else source.upc)?.trim()
            ?: return ApiResult.Success(null)
        if (!sameIdentifier(isTrack, identifier, identifier)) return ApiResult.Success(null)
        // Qobuz commonly uses zero-padded EAN-13, while Deezer indexes the equivalent UPC-A.
        val identifiers = if (isTrack) listOf(identifier) else listOf(
            identifier.trimStart('0').padStart(12, '0'),
            identifier.trimStart('0').padStart(13, '0'),
            identifier
        ).distinct()
        for (candidate in identifiers) {
            when (val response = apiCall {
                if (isTrack) deezerService.getTrack("isrc:$candidate")
                else deezerService.getAlbum("upc:$candidate")
            }) {
                is ApiResult.Success -> {
                    val metadata = response.data
                    if (metadata.error?.code == 800) continue
                    if (metadata.error != null || metadata.id == null || metadata.id <= 0) {
                        return ApiResult.Error(502)
                    }
                    if (sameIdentifier(isTrack, identifier, if (isTrack) metadata.isrc else metadata.upc)) {
                        return ApiResult.Success("https://www.deezer.com/${source.link.type.path}/${metadata.id}")
                    }
                }
                is ApiResult.Error -> return response
                is ApiResult.Exception -> return response
            }
        }
        return ApiResult.Success(null)
    }

    private suspend fun lookupIdentifier(source: Song, deezerUrl: String?): ApiResult<String?> {
        val url = deezerUrl?.toHttpUrlOrNull() ?: return ApiResult.Success(null)
        if (url.host !in setOf("deezer.com", "www.deezer.com")) return ApiResult.Success(null)
        val segments = url.pathSegments.filter { it.isNotEmpty() }
        val type = if (source.type == "song") "track" else "album"
        if (segments.size !in 2..3 || segments[segments.lastIndex - 1] != type) {
            return ApiResult.Success(null)
        }
        val id = segments.last().toLongOrNull()?.takeIf { it > 0 } ?: return ApiResult.Success(null)
        val response: ApiResult<DeezerMetadata> = apiCall {
            if (type == "track") deezerService.getTrack(id.toString())
            else deezerService.getAlbum(id.toString())
        }
        return when (response) {
            is ApiResult.Success -> {
                val metadata = response.data
                when {
                    metadata.error?.code == 800 -> ApiResult.Success(null)
                    metadata.error != null || metadata.id != id -> ApiResult.Error(502)
                    else -> ApiResult.Success(if (type == "track") metadata.isrc else metadata.upc)
                }
            }
            is ApiResult.Error -> response
            is ApiResult.Exception -> response
        }
    }

    private fun sameIdentifier(isTrack: Boolean, first: String?, second: String?): Boolean =
        if (isTrack) QobuzMatcher.sameIsrc(first, second) else QobuzMatcher.sameUpc(first, second)

}