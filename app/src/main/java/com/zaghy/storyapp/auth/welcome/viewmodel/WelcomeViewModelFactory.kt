package com.zaghy.storyapp.auth.welcome.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaghy.storyapp.di.Injection
import com.zaghy.storyapp.network.repository.StoryRepository

class WelcomeViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: WelcomeViewModelFactory? = null
        fun getInstance(context: Context): WelcomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: WelcomeViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }
}