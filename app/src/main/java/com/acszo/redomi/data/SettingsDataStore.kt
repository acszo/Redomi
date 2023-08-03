package com.acszo.redomi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acszo.redomi.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("SettingsDataStore")
        val LAYOUT_LIST_TYPE = stringPreferencesKey("list_type")
        val LAYOUT_GRID_SIZE = intPreferencesKey("grid_size")
    }

    val getLayoutListType: Flow<String?> = context.dataStore.data
            .map { preferences ->
                preferences[LAYOUT_LIST_TYPE] ?: context.resources.getString(R.string.horizontal_list)
        }

    suspend fun saveLayoutListType(listType: String) {
        context.dataStore.edit { preferences ->
            preferences[LAYOUT_LIST_TYPE] = listType
        }
    }

    val getLayoutGridSize: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[LAYOUT_GRID_SIZE] ?: 3
        }

    suspend fun saveLayoutGridSize(gridSize: Int) {
        context.dataStore.edit { preferences ->
            preferences[LAYOUT_GRID_SIZE] = gridSize
        }
    }

}