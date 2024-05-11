package com.zaghy.storyapp.auth.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaghy.storyapp.local.datastore.Muser
import com.zaghy.storyapp.network.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _navigateToHomePage = MutableLiveData<Boolean>()
    val navigateToHomePage: LiveData<Boolean>
        get() = _navigateToHomePage

    companion object {
        private const val TAG = "loginviewmodel"
    }

    init {
        _navigateToHomePage.value = false
    }

    fun login(username: String, password: String) = storyRepository.login(username, password)
    fun saveTokenAndNavigate(user: Muser) {
        viewModelScope.launch {
            try {
                storyRepository.saveUser(user)
                _navigateToHomePage.postValue(true)
            } catch (e: Exception) {
                Log.d(TAG, "Error saving token: ${e.message}")
                // Handle the error appropriately
            }
        }
    }

}