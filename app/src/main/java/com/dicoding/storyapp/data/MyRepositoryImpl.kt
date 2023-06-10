package com.dicoding.storyapp.data

import com.dicoding.storyapp.data.remote.response.*
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import com.dicoding.storyapp.domain.repository.MyRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class MyRepositoryImpl(
            private val apiService: ApiService
        ): MyRepository{

    override suspend fun register(nama: String, email: String, pass: String): SimpleResponse? {
        val response = apiService.regisUser(nama,email,pass)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun login(email: String, pass: String): LoginResponse?{
        val response = apiService.loginUser(email,pass)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun getAllStories(token: String): List<ListStoryItem>? {
        val response= apiService.getAllStories(token)
        return if (response.isSuccessful) response.body()?.listStory else null
    }

    override suspend fun uploadStory(token: String, file: MultipartBody.Part, desc: String): SimpleResponse? {
        val response= apiService.uploadStory(token, file, desc.toRequestBody("text/plain".toMediaType()))
        return if (response.isSuccessful) response.body() else null
    }
}