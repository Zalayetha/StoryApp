package com.zaghy.storyapp.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.zaghy.storyapp.addstory.model.MResponseAddStory
import com.zaghy.storyapp.auth.login.model.MResponseLogin
import com.zaghy.storyapp.auth.register.model.MResponseRegister
import com.zaghy.storyapp.detailstory.model.MResponseDetailStory
import com.zaghy.storyapp.error.ErrorResponse
import com.zaghy.storyapp.home.model.MResponseListStories
import com.zaghy.storyapp.local.datastore.Muser
import com.zaghy.storyapp.local.datastore.PreferencesDataStore
import com.zaghy.storyapp.network.Result
import com.zaghy.storyapp.network.retrofit.ApiService
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
            pref: PreferencesDataStore
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService, pref).also {
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

        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(response,ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
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
            } catch (e: HttpException) {
                val response = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(response,ErrorResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage.toString()))

            }
        }


    fun addStoryWithAuth(
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
            val response = apiService.addStories(
                description = requestBodyDescription,
                photo = multipartBody,
                latitude = requestBodyLatitude,
                longitude = requestBodyLongitude
            )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(response,ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
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
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(response,ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }


    fun listStories(
        page: Int,
        size: Int,
        location: Int
    ): LiveData<Result<MResponseListStories>?> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories(page, size, location)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(response,ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun detailStory(
        id: String
    ): LiveData<Result<MResponseDetailStory>?> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(id)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(response,ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun getUser(): LiveData<Muser> {
        return pref.getUser().asLiveData()
    }

    suspend fun saveUser(user: Muser) {
        pref.saveUser(user)
    }
}