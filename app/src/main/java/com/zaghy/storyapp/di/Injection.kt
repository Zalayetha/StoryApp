package com.zaghy.storyapp.di

import android.content.Context
import com.zaghy.storyapp.local.datastore.PreferencesDataStore
import com.zaghy.storyapp.local.datastore.dataStore
import com.zaghy.storyapp.network.repository.StoryRepository
import com.zaghy.storyapp.network.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = PreferencesDataStore.getInstance(context.dataStore)
        val user = runBlocking {
            preferences.getUser().first()
        }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, preferences)
    }
}