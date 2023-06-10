package com.dicoding.storyapp.ui.camera

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.domain.repository.MyRepository
import com.dicoding.storyapp.helper.LoginPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repo: dagger.Lazy<MyRepository>,
    private val loginPref: LoginPreference
): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadProc = MutableLiveData<Int>()
    val uploadProc: LiveData<Int> = _uploadProc

    private val _token = MutableLiveData<String?>()

    init {
        getToken()
    }

    private fun getToken() {
        _token.value = "Bearer ${loginPref.getToken()}"
    }

    fun uploadStory(file: MultipartBody.Part, desc: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repo.get().uploadStory(_token.value.toString(),file,desc)
                _uploadProc.value = 1
            } catch (e: Exception){
                Log.d(TAG, "uploadStory fail: $e")
                _uploadProc.value = 2
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setUploadDone(){
        _uploadProc.value = 3
    }

    companion object{
        const val TAG = "Camera viewmodel"
    }
}