package com.zaghy.storyapp.auth.register.viewmodel

import androidx.lifecycle.ViewModel
import com.zaghy.storyapp.network.repository.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        storyRepository.register(name, email, password)
}