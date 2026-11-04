package com.acszo.redomi.model

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

data class QobuzLink(
    val type: Type,
    val id: String
) {
    enum class Type(val path: String, val songType: String) {
        TRACK("track", "song"),
        ALBUM("album", "album")
    }

    val url: String get() = "https://open.qobuz.com/${type.path}/$id"
    val entityUniqueId: String get() = "QOBUZ_${type.songType.uppercase()}::$id"

    companion object {
        private val hosts = setOf("open.qobuz.com", "play.qobuz.com", "www.qobuz.com", "qobuz.com")
        private val locale = Regex("[a-zA-Z]{2}-[a-zA-Z]{2}")
        private val albumId = Regex("[a-zA-Z0-9]+")

        fun isQobuzUrl(value: String): Boolean = value.toHttpUrlOrNull()?.host in hosts

        fun parse(value: String): QobuzLink? {
            val url = value.toHttpUrlOrNull() ?: return null
            if (url.host !in hosts || url.username.isNotEmpty() || url.password.isNotEmpty()) return null
            if (url.port != if (url.isHttps) 443 else 80) return null
            val segments = url.encodedPath.trimEnd('/').removePrefix("/").split('/')
            val (kind, id) = when {
                segments.size == 2 -> segments[0] to segments[1]
                segments.size == 4 && locale.matches(segments[0]) &&
                    segments[1] == "album" && segments[2].isNotEmpty() -> "album" to segments[3]
                else -> return null
            }
            return when (kind) {
                "track" -> id.takeIf { it.all(Char::isDigit) }
                    ?.toLongOrNull()?.takeIf { it > 0 }
                    ?.let { QobuzLink(Type.TRACK, it.toString()) }
                "album" -> id.takeIf { albumId.matches(it) }?.let { QobuzLink(Type.ALBUM, it) }
                else -> null
            }
        }
    }
}
