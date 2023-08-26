package com.acszo.redomi.model

import com.acszo.redomi.model.Platform.platforms
import kotlinx.serialization.Serializable

@Serializable
data class AppsConfig(
    val installedApps: List<AppDetails> = platforms,
    val allApps: List<AppDetails> = platforms
)
