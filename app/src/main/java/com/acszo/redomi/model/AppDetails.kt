package com.acszo.redomi.model

data class AppDetails(
    val title: String,
    val icon: Int,
    val packageName: List<String>,
    var link: String
)
