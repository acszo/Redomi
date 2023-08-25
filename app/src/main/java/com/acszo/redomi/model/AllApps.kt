package com.acszo.redomi.model

import com.acszo.redomi.model.Platform.platforms
import kotlinx.serialization.Serializable

@Serializable
data class AllApps(
    val apps: List<AppDetails> = platforms
)
