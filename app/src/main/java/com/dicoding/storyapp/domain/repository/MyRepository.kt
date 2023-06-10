package com.dicoding.storyapp.domain.repository

import com.dicoding.storyapp.data.remote.response.*
import okhttp3.MultipartBody

interface MyRepository {
    suspend fun register(nama: String, email: String, pass: String): SimpleResponse?
    suspend fun login(email: String, pass: String): LoginResponse?
    suspend fun getAllStories(token: String): List<ListStoryItem>?
    suspend fun uploadStory(token: String, file: MultipartBody.Part, desc: String): SimpleResponse?
}