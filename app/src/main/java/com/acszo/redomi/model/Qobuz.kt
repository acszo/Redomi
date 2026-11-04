package com.acszo.redomi.model

import kotlinx.serialization.Serializable

@Serializable
data class QobuzArtist(val name: String? = null)

@Serializable
data class QobuzImage(
    val large: String? = null,
    val small: String? = null,
    val thumbnail: String? = null
) {
    val url: String? get() = large ?: small ?: thumbnail
}

@Serializable
data class QobuzAlbum(
    val id: String,
    val title: String? = null,
    val version: String? = null,
    val artist: QobuzArtist? = null,
    val image: QobuzImage? = null,
    val upc: String? = null,
    val streamable: Boolean? = null
) {
    fun toEntity(): QobuzEntity? {
        val link = QobuzLink.parse("https://open.qobuz.com/album/$id") ?: return null
        val name = title?.takeIf { it.isNotBlank() } ?: return null
        val artistName = artist?.name?.takeIf { it.isNotBlank() } ?: return null
        return QobuzEntity(link, withVersion(name, version), artistName, image?.url, upc = upc,
            streamable = streamable != false)
    }
}

@Serializable
data class QobuzTrack(
    val id: Long,
    val title: String? = null,
    val version: String? = null,
    val performer: QobuzArtist? = null,
    val album: QobuzAlbum? = null,
    val isrc: String? = null,
    val streamable: Boolean? = null
) {
    fun toEntity(): QobuzEntity? {
        if (id <= 0) return null
        val name = title?.takeIf { it.isNotBlank() } ?: return null
        val artistName = performer?.name?.takeIf { it.isNotBlank() }
            ?: album?.artist?.name?.takeIf { it.isNotBlank() } ?: return null
        return QobuzEntity(QobuzLink(QobuzLink.Type.TRACK, id.toString()),
            withVersion(name, version), artistName, album?.image?.url, isrc = isrc,
            streamable = streamable != false)
    }
}

@Serializable
data class QobuzTracks(val items: List<QobuzTrack>)

@Serializable
data class QobuzAlbums(val items: List<QobuzAlbum>)

@Serializable
data class QobuzTrackSearch(val tracks: QobuzTracks)

@Serializable
data class QobuzAlbumSearch(val albums: QobuzAlbums)

data class QobuzEntity(
    val link: QobuzLink,
    val title: String,
    val artistName: String,
    val thumbnailUrl: String?,
    val isrc: String? = null,
    val upc: String? = null,
    val streamable: Boolean = true
) {
    fun toSong(): Song = Song(
        platform = "qobuz",
        link = link.url,
        type = link.type.songType,
        title = title,
        artistName = artistName,
        thumbnailUrl = thumbnailUrl
    )

    fun toProviders(): Providers = Providers(
        entityUniqueId = link.entityUniqueId,
        linksByPlatform = mapOf("qobuz" to Link(link.url, link.entityUniqueId)),
        entitiesByUniqueId = mapOf(link.entityUniqueId to toSong())
    )
}

private fun withVersion(title: String, version: String?): String =
    if (version.isNullOrBlank() || title.contains(version.trim(), ignoreCase = true)) title.trim()
    else "${title.trim()} (${version.trim()})"
