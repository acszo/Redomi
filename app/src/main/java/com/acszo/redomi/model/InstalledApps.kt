package com.acszo.redomi.model

import com.acszo.redomi.model.AppDetails
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class InstalledApps(
    val apps: PersistentList<AppDetails> = persistentListOf()
)
