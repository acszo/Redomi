package com.acszo.redomi.utils

import okhttp3.HttpUrl.Companion.toHttpUrl

object QobuzBootstrap {
    private val baseUrl = "https://open.qobuz.com/".toHttpUrl()
    private val scripts = Regex("""<script\b[^>]*\bsrc\s*=\s*["']([^"']+)["']""", RegexOption.IGNORE_CASE)
    private val bundlePath = Regex("""/resources/[^/]+/js/main(?:\.[a-zA-Z0-9_-]+)?\.js""")
    private val appIds = Regex("""["']?app_id["']?\s*:\s*["']([0-9]+)["']""")

    fun bundleUrl(html: String): String? = scripts.findAll(html)
        .mapNotNull { baseUrl.resolve(it.groupValues[1]) }
        .filter { it.isHttps && it.host == baseUrl.host && it.port == 443 &&
            it.username.isEmpty() && it.password.isEmpty() && bundlePath.matches(it.encodedPath) }
        .map { it.toString() }
        .distinct()
        .singleOrNull()

    fun appId(script: String): String? = appIds.findAll(script)
        .map { it.groupValues[1] }
        .distinct()
        .singleOrNull()
}
