package com.acszo.redomi.repository

import android.content.Context
import androidx.datastore.dataStore
import com.acszo.redomi.data.AllAppSerializable
import com.acszo.redomi.model.AppDetails
import kotlinx.coroutines.flow.first

val Context.dataStore by dataStore("selected-apps", AllAppSerializable)

class DataStoreRepository(
    private val context: Context
) {

    suspend fun readInstalledApps(): List<AppDetails> {
        return context.dataStore.data.first().installedApps
    }

    suspend fun saveInstalledApps(installedApps: List<AppDetails>) {
        context.dataStore.updateData {
            it.copy(installedApps = installedApps)
        }
    }

    suspend fun readAllApps(): List<AppDetails> {
        return context.dataStore.data.first().allApps
    }

    suspend fun saveAllApps(allApps: List<AppDetails>) {
        context.dataStore.updateData {
            it.copy(allApps = allApps)
        }
    }

}