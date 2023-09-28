package com.acszo.redomi.utils

object StringUtil {

    fun separateUppercase(text: String): String {
        return text.replace("(?<=[^A-Z])(?=[A-Z])".toRegex(), " ")
            .replaceFirstChar { it.uppercase() }
    }

}