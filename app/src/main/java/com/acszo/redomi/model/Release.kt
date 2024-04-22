package com.acszo.redomi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Release(
    val assets: List<Asset>,
    val body: String,
    val name: String,
    @SerialName("tag_name")
    val tagName: String,
)

@Serializable
data class Asset(
    @SerialName("browser_download_url")
    val browserDownloadUrl: String,
)