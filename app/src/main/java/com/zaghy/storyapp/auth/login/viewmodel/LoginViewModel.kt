package com.zaghy.storyapp.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaghy.storyapp.network.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository):ViewModel() {
    fun login(username:String,password:String) = storyRepository.login(username,password)
    fun saveToken(token:String){
        viewModelScope.launch {
            storyRepository.saveToken(token)
        }
    }

}