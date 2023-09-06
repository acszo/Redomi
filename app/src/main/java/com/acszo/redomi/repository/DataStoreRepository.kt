package com.acszo.redomi.repository

import android.content.Context
import androidx.datastore.dataStore
import com.acszo.redomi.data.AllAppSerializable
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.model.AppsConfig
import kotlinx.coroutines.flow.Flow

val Context.dataStore by dataStore("selected-apps", AllAppSerializable)

class DataStoreRepository(
    private val context: Context
) {

    fun readDataStore(): Flow<AppsConfig> {
        return context.dataStore.data
    }

    suspend fun saveInstalledApps(installedApps: List<AppDetails>) {
        context.dataStore.updateData {
            it.copy(installedApps = installedApps)
        }
    }

    suspend fun saveAllApps(allApps: List<AppDetails>) {
        context.dataStore.updateData {
            it.copy(allApps = allApps)
        }
    }

}