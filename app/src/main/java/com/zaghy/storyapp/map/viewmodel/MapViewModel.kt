package com.zaghy.storyapp.map.viewmodel

import androidx.lifecycle.ViewModel
import com.zaghy.storyapp.network.repository.StoryRepository

class MapViewModel(private val storyRepository: StoryRepository):ViewModel() {
    fun getStoriesByLocation(token:String,location:Int = 1) = storyRepository.getListStoriesByLocation(token = token, location = location)
    fun getUser() = storyRepository.getUser()

}