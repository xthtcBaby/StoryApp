package com.dicoding.storyapp.helper

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginPreference @Inject constructor(
        @ApplicationContext context: Context
    ){

    private val loginPref : SharedPreferences
    val editor : SharedPreferences.Editor

    init {
        loginPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = loginPref.edit()
    }

    fun setToken(token: String) {
        editor.putString(TOKEN, token)
            .apply()
    }

    fun getToken(): String?{
        return loginPref.getString(TOKEN,null)
    }

    fun setIsLogin(status: Boolean){
        editor.putBoolean(IS_LOGIN, status)
            .apply()
    }

    fun isLogin(): Boolean = loginPref.getBoolean(IS_LOGIN, false)

    fun logOut(){
        editor.clear()
            .apply()
    }

    companion object{
        private const val PREFS_NAME = "login_pref"
        private const val TOKEN = "login_token"
        private const val IS_LOGIN = "is_login"
    }
}