package com.acszo.redomi.model

import com.acszo.redomi.model.Platform.platforms
import kotlinx.serialization.Serializable

@Serializable
data class AppsPreferences(
    val openingAppsSelection: List<AppDetails> = platforms,
    val sharingAppsSelection: List<AppDetails> = platforms
)
