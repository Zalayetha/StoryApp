package com.zaghy.storyapp.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaghy.storyapp.auth.login.viewmodel.LoginViewModelFactory
import com.zaghy.storyapp.di.Injection
import com.zaghy.storyapp.network.repository.StoryRepository

class HomeViewModelFactory(private val storyRepository: StoryRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
    companion object{
        @Volatile
        private var instance: HomeViewModelFactory? = null
        fun getInstance(context: Context): HomeViewModelFactory =
            instance ?: synchronized(this){
                instance ?: HomeViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }
}