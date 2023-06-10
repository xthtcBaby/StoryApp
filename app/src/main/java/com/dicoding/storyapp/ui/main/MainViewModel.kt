package com.dicoding.storyapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.domain.repository.MyRepository
import com.dicoding.storyapp.helper.LoginPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: dagger.Lazy<MyRepository>,
    private val loginPref: LoginPreference
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _token = MutableLiveData<String?>()

    init {
        getToken()
    }

    private fun getToken() {
        _token.value = "Bearer ${loginPref.getToken()}"
    }

    fun getAllStory(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _listStory.value = repo.get().getAllStories(_token.value!!)
            } catch (e: Exception){
                Log.d(TAG, "getAllStory fail: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logOut(){
        loginPref.logOut()
        _token.value = null
    }

    companion object{
        const val TAG = "Main View Model"
    }
}