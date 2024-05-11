package com.zaghy.storyapp.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zaghy.storyapp.network.repository.StoryRepository

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    companion object{
        private const val TAG = "HomeViewModel"
    }
    init {
        Log.d(TAG, "init: ")
    }

    fun getStories() = storyRepository.listStories(
        page = 1,
        size = 20,
        location = 0
    )


}