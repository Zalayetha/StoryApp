package com.zaghy.storyapp.network.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.zaghy.storyapp.addstory.model.MResponseAddStory
import com.zaghy.storyapp.auth.login.model.MResponseLogin
import com.zaghy.storyapp.auth.register.model.MResponseRegister
import com.zaghy.storyapp.detailstory.model.MResponseDetailStory
import com.zaghy.storyapp.home.model.MResponseListStories
import com.zaghy.storyapp.local.datastore.Muser
import com.zaghy.storyapp.local.datastore.PreferencesDataStore
import com.zaghy.storyapp.network.Result
import com.zaghy.storyapp.network.retrofit.ApiService
import com.zaghy.storyapp.utils.Utilities
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val pref: PreferencesDataStore
) {
    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref:PreferencesDataStore
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService,pref).also {
                instance = it
            }
        }

        private const val TAG = "StoryRepository"
    }

    fun login(email: String, password: String): LiveData<Result<MResponseLogin>?> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<MResponseRegister>?> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)

                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))

            }
        }


    fun addStoryWithAuth(
        image: File,
        description: String,
        token: String,
        latitude: Float,
        longitude: Float
    ): LiveData<Result<MResponseAddStory>?> = liveData {
        emit(Result.Loading)
        try {
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
            val requestBodyLatitude = latitude.toString().toRequestBody("text/plain".toMediaType())
            val requestBodyLongitude =
                longitude.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
            val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                image.name,
                requestImageFile
            )
            val response = apiService.addStories(
                token = token,
                description = requestBodyDescription,
                photo = multipartBody,
                latitude = requestBodyLatitude,
                longitude = requestBodyLongitude
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addStoryGuest(
        image: File,
        description: String,
        latitude: Float,
        longitude: Float
    ): LiveData<Result<MResponseAddStory>?> = liveData {
        emit(Result.Loading)
        try {
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
            val requestBodyLatitude = latitude.toString().toRequestBody("text/plain".toMediaType())
            val requestBodyLongitude =
                longitude.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
            val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                image.name,
                requestImageFile
            )
            val response = apiService.addStoriesGuest(
                description = requestBodyDescription,
                photo = multipartBody,
                latitude = requestBodyLatitude,
                longitude = requestBodyLongitude
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }


    fun listStories(
        page: Int,
        size: Int,
        location: Int
    ): LiveData<Result<MResponseListStories>?> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories( page, size, location)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun detailStory(
        token: String,
        id: String
    ): LiveData<Result<MResponseDetailStory>?> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(token, id)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUser(): LiveData<Muser> {
        return pref.getUser().asLiveData()
    }

     suspend fun saveUser(user: Muser) {
        pref.saveUser(user)
    }
}