package com.zaghy.storyapp.addstory.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaghy.storyapp.detailstory.viewmodel.DetailStoryViewModel
import com.zaghy.storyapp.detailstory.viewmodel.DetailStoryViewModelFactory
import com.zaghy.storyapp.di.Injection
import com.zaghy.storyapp.network.repository.StoryRepository

class AddStoryViewModelFactory(private val storyRepository: StoryRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
    companion object{
        @Volatile
        private var instance: AddStoryViewModelFactory? = null
        fun getInstance(context: Context): AddStoryViewModelFactory =
            instance ?: synchronized(this){
                instance ?: AddStoryViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }
}