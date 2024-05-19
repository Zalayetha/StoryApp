package com.zaghy.storyapp.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zaghy.storyapp.home.model.ListStoryItem
import com.zaghy.storyapp.home.model.MResponseListStories
import com.zaghy.storyapp.network.repository.StoryRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _navigateToLoginPage = MutableLiveData<Boolean>()
    val navigateToLoginPage:LiveData<Boolean> get() = _navigateToLoginPage


    companion object{
        private const val TAG = "HomeViewModel"
    }
    init {
        _navigateToLoginPage.value = false
    }
    fun getStories(token:String) = storyRepository.listStories(
        token = token,
    ).cachedIn(viewModelScope)




    fun logout(){
        viewModelScope.launch {
            try{
                storyRepository.clearUser()
                _navigateToLoginPage.postValue(true)
            }catch(e:Exception){
                Log.d(TAG, "logout: ${e.message.toString()}")
            }

        }
    }

    fun getUser() = storyRepository.getUser()



}