package com.zaghy.storyapp.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zaghy.storyapp.network.repository.StoryRepository

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.listStories(
        page = 1,
        size = 20,
        location = 0
    )
}