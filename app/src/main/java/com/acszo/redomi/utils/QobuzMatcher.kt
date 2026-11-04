package com.acszo.redomi.utils

import com.acszo.redomi.model.QobuzEntity
import com.acszo.redomi.model.Song
import java.text.Normalizer
import java.util.Locale

object QobuzMatcher {
    fun match(source: Song, candidates: List<QobuzEntity>, identifier: String?): QobuzEntity? {
        val available = candidates.filter { it.streamable && it.link.type.songType == source.type }
        if (!identifier.isNullOrBlank()) {
            return available.firstOrNull {
                if (source.type == "song") sameIsrc(identifier, it.isrc)
                else sameUpc(identifier, it.upc)
            }
        }
        val title = normalize(source.title ?: return null)
        val artist = normalize(source.artistName ?: return null)
        if (title.isEmpty() || artist.isEmpty()) return null
        return available.filter { normalize(it.title) == title && normalize(it.artistName) == artist }
            .distinctBy { it.link }
            .singleOrNull()
    }

    fun sameIsrc(first: String?, second: String?): Boolean =
        !first.isNullOrBlank() && !second.isNullOrBlank() &&
            first.trim().equals(second.trim(), ignoreCase = true)

    fun sameUpc(first: String?, second: String?): Boolean {
        if (first.isNullOrBlank() || second.isNullOrBlank() ||
            !first.all(Char::isDigit) || !second.all(Char::isDigit)) return false
        val normalized = first.trimStart('0')
        return normalized.isNotEmpty() && normalized == second.trimStart('0')
    }

    private fun normalize(value: String): String =
        Normalizer.normalize(value, Normalizer.Form.NFKD)
            .replace(Regex("\\p{M}+"), "")
            .lowercase(Locale.ROOT)
            .replace(Regex("[^\\p{L}\\p{N}]+"), " ")
            .trim()
}
