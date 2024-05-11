package com.zaghy.storyapp.di

import android.content.Context
import com.zaghy.storyapp.local.datastore.PreferencesDataStore
import com.zaghy.storyapp.local.datastore.dataStore
import com.zaghy.storyapp.network.repository.StoryRepository
import com.zaghy.storyapp.network.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val preferences = PreferencesDataStore.getInstance(context.dataStore)
        return StoryRepository.getInstance(apiService,preferences)
    }
}