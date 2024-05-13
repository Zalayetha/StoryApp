package com.zaghy.storyapp.auth.welcome.viewmodel

import androidx.lifecycle.ViewModel
import com.zaghy.storyapp.network.repository.StoryRepository

class WelcomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getUser() = storyRepository.getUser()
}