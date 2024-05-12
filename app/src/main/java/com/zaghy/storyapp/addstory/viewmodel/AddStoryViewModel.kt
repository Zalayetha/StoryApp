package com.zaghy.storyapp.addstory.viewmodel

import androidx.lifecycle.ViewModel
import com.zaghy.storyapp.network.repository.StoryRepository
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository):ViewModel() {
    fun addStory(image: File,
                 description: String,
                 latitude: Float,
                 longitude: Float) = storyRepository.addStoryWithAuth(image=image, description = description,latitude=latitude,longitude=longitude)


}