package com.acszo.redomi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acszo.redomi.data.DataStoreConst.HORIZONTAL_LIST
import com.acszo.redomi.data.DataStoreConst.MEDIUM_GRID
import com.acszo.redomi.data.DataStoreConst.SYSTEM_THEME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("SettingsDataStore")
        val FIRST_TIME = booleanPreferencesKey("first_time")
        val LAYOUT_LIST_TYPE = intPreferencesKey("list_type")
        val LAYOUT_GRID_SIZE = intPreferencesKey("grid_size")
        val THEME_MODE = intPreferencesKey("theme_mode")
    }

    val getIsFirstTime: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[FIRST_TIME] ?: true
        }

    suspend fun saveIsFirstTime(isFirstTime: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FIRST_TIME] = isFirstTime
        }
    }

    val getLayoutListType: Flow<Int?> = context.dataStore.data
            .map { preferences ->
                preferences[LAYOUT_LIST_TYPE] ?: HORIZONTAL_LIST
        }

    suspend fun saveLayoutListType(listType: Int) {
        context.dataStore.edit { preferences ->
            preferences[LAYOUT_LIST_TYPE] = listType
        }
    }

    val getLayoutGridSize: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[LAYOUT_GRID_SIZE] ?: MEDIUM_GRID
        }

    suspend fun saveLayoutGridSize(gridSize: Int) {
        context.dataStore.edit { preferences ->
            preferences[LAYOUT_GRID_SIZE] = gridSize
        }
    }

    val getThemeMode: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_MODE] ?: SYSTEM_THEME
        }

    suspend fun saveThemeMode(themeMode: Int) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode
        }
    }

}