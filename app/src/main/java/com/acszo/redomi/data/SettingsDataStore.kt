package com.acszo.redomi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acszo.redomi.model.Platform.platforms
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

    fun getString(key: String): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(key)]
            }
    }

    suspend fun setString(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    fun getSetOfStrings(key: String): Flow<Set<String>> {
        return context.dataStore.data
            .map { preferences ->
                preferences[stringSetPreferencesKey(key)] ?: platforms.keys
            }
    }

    suspend fun setSetOfStrings(key: String, value: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[stringSetPreferencesKey(key)] = value
        }
    }

}