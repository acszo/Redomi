package com.acszo.redomi.repository

import com.acszo.redomi.model.QobuzEntity
import com.acszo.redomi.model.QobuzLink
import com.acszo.redomi.model.Song
import com.acszo.redomi.service.ApiResult
import com.acszo.redomi.service.QobuzService
import com.acszo.redomi.service.apiCall
import com.acszo.redomi.utils.QobuzBootstrap
import com.acszo.redomi.utils.QobuzMatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class QobuzRepository(private val service: QobuzService) {
    private val appIdLock = Mutex()
    private var cachedAppId: String? = null

    suspend fun getEntity(link: QobuzLink): ApiResult<QobuzEntity> = when (link.type) {
        QobuzLink.Type.TRACK -> catalogCall { service.getTrack(link.id, it) }
            .mapEntity(link) { it.toEntity() }
        QobuzLink.Type.ALBUM -> catalogCall { service.getAlbum(link.id, it) }
            .mapEntity(link) { it.toEntity() }
    }

    suspend fun findMatch(source: Song, identifier: String?): ApiResult<QobuzEntity?> {
        val queries = listOfNotNull(identifier?.takeIf { it.isNotBlank() },
            "${source.title.orEmpty()} ${source.artistName.orEmpty()}".trim()).distinct()
        for (query in queries) {
            val candidates = search(source.type, query)
            when (candidates) {
                is ApiResult.Success -> {
                    val match = QobuzMatcher.match(source, candidates.data, identifier)
                    if (match != null) return ApiResult.Success(match)
                }
                is ApiResult.Error -> return candidates
                is ApiResult.Exception -> return candidates
            }
        }
        return ApiResult.Success(null)
    }

    private suspend fun search(type: String?, query: String): ApiResult<List<QobuzEntity>> =
        when (type) {
            "song" -> when (val result = catalogCall { service.searchTracks(query, it) }) {
                is ApiResult.Success -> ApiResult.Success(result.data.tracks.items.mapNotNull { it.toEntity() })
                is ApiResult.Error -> result
                is ApiResult.Exception -> result
            }
            "album" -> when (val result = catalogCall { service.searchAlbums(query, it) }) {
                is ApiResult.Success -> ApiResult.Success(result.data.albums.items.mapNotNull { it.toEntity() })
                is ApiResult.Error -> result
                is ApiResult.Exception -> result
            }
            else -> ApiResult.Error(400)
        }

    private suspend fun <T : Any> catalogCall(request: suspend (String) -> Response<T>): ApiResult<T> {
        val result = apiCall { request(appId()) }
        if (result is ApiResult.Error && result.code in setOf(401, 403)) {
            appIdLock.withLock { cachedAppId = null }
            return apiCall { request(appId()) }
        }
        return result
    }

    private suspend fun appId(): String = appIdLock.withLock {
        cachedAppId ?: run {
            // This is the public client identifier used by Qobuz's share page, not a user token or secret.
            val bundleUrl = QobuzBootstrap.bundleUrl(service.getWebPage().readBody())
                ?: throw IllegalStateException("Qobuz share-page bundle was not found")
            val appId = QobuzBootstrap.appId(service.getWebBundle(bundleUrl).readBody())
                ?: throw IllegalStateException("Qobuz public catalog identifier was not found")
            cachedAppId = appId
            appId
        }
    }

    private fun Response<ResponseBody>.readBody(): String {
        if (!isSuccessful) {
            errorBody()?.close()
            throw HttpException(this)
        }
        return body()?.use { it.string() } ?: throw IllegalStateException("Empty Qobuz bootstrap response")
    }

    private fun <T> ApiResult<T>.mapEntity(
        expected: QobuzLink,
        transform: (T) -> QobuzEntity?
    ): ApiResult<QobuzEntity> = when (this) {
        is ApiResult.Success -> transform(data)?.takeIf { it.link == expected }
            ?.let { ApiResult.Success(it) } ?: ApiResult.Error(502)
        is ApiResult.Error -> this
        is ApiResult.Exception -> this
    }
}
