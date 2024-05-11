package com.zaghy.storyapp.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zaghy.storyapp.auth.login.model.LoginResult
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

//    fun getToken(): Flow<String> {
//        return dataStore.data.map { preferences ->
//            preferences[TOKEN_KEY] ?: ""
//        }
//    }

//    suspend fun saveToken(token: String) {
//        Log.d("pref", "about to saveToken:")
//        try {
//            dataStore.edit { preferences ->
//                preferences[TOKEN_KEY] = token
//                Log.d("pref", "Token saved successfully")
//            }
//        } catch (e: Exception) {
//            Log.d("pref", "Failed to save token: ${e.message}")
//        }
//    }

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