package com.zaghy.storyapp.di

import android.content.Context
import com.zaghy.storyapp.local.datastore.PreferencesDataStore
import com.zaghy.storyapp.local.datastore.dataStore
import com.zaghy.storyapp.local.room.StoriesDatabase
import com.zaghy.storyapp.network.repository.StoryRepository
import com.zaghy.storyapp.network.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = PreferencesDataStore.getInstance(context.dataStore)
        val database = StoriesDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService, preferences,database)
    }
}