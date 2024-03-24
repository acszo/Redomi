package com.acszo.redomi.model

import kotlinx.serialization.Serializable

@Serializable
data class AppDetails(
    val title: String,
    val icon: Int
)
