package com.zaghy.storyapp.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesDataStore private constructor(private val dataStore: DataStore<Preferences>) {

    private object Key {
        val token = stringPreferencesKey("token")
        val username = stringPreferencesKey("username")
        val id = stringPreferencesKey("id")

    }

    //    private val TOKEN_KEY = stringPreferencesKey("token")
    private inline val Preferences.id
        get() = this[Key.id] ?: ""
    private inline val Preferences.name
        get() = this[Key.username] ?: ""
    private inline val Preferences.token
        get() = this[Key.token] ?: ""


    fun getUser(): Flow<Muser> {

        return dataStore.data.map { preferences ->
            Muser(
                preferences.id,
                preferences.name,
                preferences.token
            )
        }
    }

    suspend fun saveUser(user: Muser) {
        dataStore.edit { preferences ->
            preferences[Key.id] = user.id
            preferences[Key.username] = user.name
            preferences[Key.token] = user.token
        }
    }

    suspend fun clearUser(){
        dataStore.edit { preferences->
            preferences.clear()
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: PreferencesDataStore? = null
        fun getInstance(dataStore: DataStore<Preferences>): PreferencesDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferencesDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}