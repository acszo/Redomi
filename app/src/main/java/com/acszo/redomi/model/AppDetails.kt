package com.acszo.redomi.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
data class AppDetails(
    val title: String,
    @DrawableRes val icon: Int
)
