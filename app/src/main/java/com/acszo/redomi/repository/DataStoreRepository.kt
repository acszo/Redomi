package com.acszo.redomi.repository

import android.content.Context
import androidx.datastore.dataStore
import com.acszo.redomi.data.AllAppSerializable
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.ui.settings.dataStore
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.first

val Context.dataStore by dataStore("selected-apps", AllAppSerializable)

class DataStoreRepository {

    suspend fun getAllApps(context: Context): List<AppDetails> {
        return context.dataStore.data.first().apps
    }

    suspend fun setAllApps(context: Context, allApps: PersistentList<AppDetails>) {
        context.dataStore.updateData {
            it.copy(apps = allApps)
        }
    }

}