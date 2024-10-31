package com.acszo.redomi.model

import com.acszo.redomi.R
import com.acszo.redomi.data.Resource
import com.acszo.redomi.model.Platform.platforms
import kotlinx.serialization.Serializable

@Serializable
data class AppsPreferences(
    val openingAppsSelection: List<String> = platforms.keys.toList(),
    val sharingAppsSelection: List<String> = platforms.keys.toList()
)

enum class AppList: Resource {
    OPENING,
    SHARING;

    override val toRes: Int
        get() = when (this) {
            OPENING -> R.string.opening
            SHARING -> R.string.sharing
        }
}