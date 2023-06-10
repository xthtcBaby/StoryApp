package com.dicoding.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.remote.response.SimpleResponse
import com.dicoding.storyapp.domain.repository.MyRepository
import com.dicoding.storyapp.helper.LoginPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: dagger.Lazy<MyRepository>,
    private val loginPref: LoginPreference
): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginProc = MutableLiveData<Int>()
    val loginProc: LiveData<Int> = _loginProc

    private val _isRegistered = MutableLiveData<SimpleResponse>()
    val isRegistered: LiveData<SimpleResponse> = _isRegistered

    fun register(nama: String, email: String, pass: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isRegistered.value = repo.get().register(nama,email,pass)
            } catch (e: Exception){
                Log.d(TAG, "register fail: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, pass: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val loginRes = repo.get().login(email,pass)
                if (loginRes != null){
                    loginPref.setToken(loginRes.loginResult.token)
                    loginPref.setIsLogin(true)
                    _loginProc.value = 1
                }
                else
                    _loginProc.value = 2
            } catch (e: Exception){
                Log.d(TAG, "login fail: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setLoginDone(){
        _loginProc.value = 3
    }

    companion object{
        private const val TAG = "Auth Viewmodel"
    }


}