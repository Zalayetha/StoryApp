package com.zaghy.storyapp.detailstory.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaghy.storyapp.di.Injection
import com.zaghy.storyapp.network.repository.StoryRepository

class DetailStoryViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailStoryViewModelFactory? = null
        fun getInstance(context: Context): DetailStoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailStoryViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }
}