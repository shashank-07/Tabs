package com.example.tabs.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "tabs_datastore"
    )

    val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }
    val cluster: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[CLUSTER_NUM]
        }


    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }
    suspend fun saveCluster(cluster: Int) {
        dataStore.edit { preferences ->
            preferences[CLUSTER_NUM] = cluster
        }
    }
    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_AUTH = preferencesKey<String>("key_auth")
        private val CLUSTER_NUM= preferencesKey<Int>("cluster number")
    }

}