package com.acszo.redomi.model

import com.google.gson.annotations.SerializedName

data class Release(
    val assets: List<Asset>,
    val body: String,
    val name: String,
    @SerializedName("tag_name")
    val tagName: String,
)

data class Asset(
    @SerializedName("browser_download_url")
    val browserDownloadUrl: String,
)