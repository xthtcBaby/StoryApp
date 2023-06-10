package com.dicoding.storyapp.data.remote.retrofit

import com.dicoding.storyapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun regisUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ):Response<SimpleResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ):Response<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String
    ): Response<GetAllStoriesResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<SimpleResponse>
}