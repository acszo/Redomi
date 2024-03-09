package com.acszo.redomi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(
    private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("SettingsDataStore")
    }

    fun getBoolean(key: String): Flow<Boolean?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[booleanPreferencesKey(key)]
            }
    }

    suspend fun setBoolean(key: String, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    fun getInt(key: String): Flow<Int?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[intPreferencesKey(key)]
            }
    }

    suspend fun setInt(key: String, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }

}