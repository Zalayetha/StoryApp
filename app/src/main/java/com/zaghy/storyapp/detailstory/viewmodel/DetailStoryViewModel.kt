package com.zaghy.storyapp.detailstory.viewmodel

import androidx.lifecycle.ViewModel
import com.zaghy.storyapp.network.repository.StoryRepository

class DetailStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getDetailStory(token:String,id: String) = storyRepository.detailStory(token,id)
    fun getUser()=storyRepository.getUser()
}