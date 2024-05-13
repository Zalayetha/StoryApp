package com.zaghy.storyapp.network.retrofit

import com.zaghy.storyapp.addstory.model.MResponseAddStory
import com.zaghy.storyapp.auth.login.model.MResponseLogin
import com.zaghy.storyapp.auth.register.model.MResponseRegister
import com.zaghy.storyapp.detailstory.model.MResponseDetailStory
import com.zaghy.storyapp.home.model.MResponseListStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name")
        name: String,
        @Field("email")
        email: String,
        @Field("password")
        password: String
    ): MResponseRegister

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email")
        email: String,
        @Field("password")
        password: String
    ): MResponseLogin


    @Multipart
    @POST("stories")
    suspend fun addStories(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") latitude: RequestBody,
        @Part("lon") longitude: RequestBody
    ): MResponseAddStory

    @Multipart
    @POST("stories")
    suspend fun addStoriesGuest(
        @Part("description") description: RequestBody,
        @Part("photo") photo: MultipartBody.Part,
        @Part("lat") latitude: RequestBody,
        @Part("lon") longitude: RequestBody
    ): MResponseAddStory

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): MResponseListStories


    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): MResponseDetailStory


}