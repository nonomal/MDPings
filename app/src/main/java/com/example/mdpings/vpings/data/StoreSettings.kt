package com.example.mdpings.vpings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreSettings(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        val USER_API_BACKEND = stringPreferencesKey("user_api_backend")
        val USER_API_TOKEN = stringPreferencesKey("user_api_token")
        val INTERVAL = intPreferencesKey("interval")
    }

    val getApi: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_API_BACKEND] ?: ""
        }

    suspend fun saveApi(api: String) {
        context.dataStore.edit { settings ->
            settings[USER_API_BACKEND] = api
        }
    }

    val getToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_API_TOKEN] ?: ""
        }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { settings ->
            settings[USER_API_TOKEN] = token
        }
    }

    val getInterval: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[INTERVAL] ?: 5
        }

    suspend fun saveInterval(interval: Int) {
        context.dataStore.edit { settings ->
            settings[INTERVAL] = interval
        }
    }

}