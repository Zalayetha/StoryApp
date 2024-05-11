package com.zaghy.storyapp.auth.login.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaghy.storyapp.di.Injection
import com.zaghy.storyapp.network.repository.StoryRepository

class LoginViewModelFactory(private val storyRepository: StoryRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
    companion object{
        @Volatile
        private var instance: LoginViewModelFactory? = null
        fun getInstance(context: Context): LoginViewModelFactory =
            instance ?: synchronized(this){
                instance ?: LoginViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }

}