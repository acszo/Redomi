package com.acszo.redomi.utils

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object SharedLink {
    private val urls = Regex("""https?://[^\s<>"']+""", RegexOption.IGNORE_CASE)

    fun extract(text: String?): String? {
        var url = urls.find(text ?: return null)?.value?.trimEnd('.', ',', ';', '!') ?: return null
        for ((open, close) in listOf('(' to ')', '[' to ']', '{' to '}')) {
            while (url.endsWith(close) && url.count { it == close } > url.count { it == open }) {
                url = url.dropLast(1)
            }
        }
        return url.takeIf { it.toHttpUrlOrNull() != null }
    }
}
