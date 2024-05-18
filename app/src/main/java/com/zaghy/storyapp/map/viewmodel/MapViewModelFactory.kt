package com.zaghy.storyapp.map.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaghy.storyapp.di.Injection
import com.zaghy.storyapp.home.viewmodel.HomeViewModel
import com.zaghy.storyapp.home.viewmodel.HomeViewModelFactory
import com.zaghy.storyapp.network.repository.StoryRepository

class MapViewModelFactory(private val storyRepository: StoryRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MapViewModelFactory? = null
        fun getInstance(context: Context): MapViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MapViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }
}