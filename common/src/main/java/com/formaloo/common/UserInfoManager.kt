package com.formaloo.common

import android.content.Context
import android.content.SharedPreferences

class UserInfoManager(context: Context) {
    private val sharedPreferences: SharedPreferences
    private val SESSION_TOKEN = "sessionToken"
    private val AUTHORIZATION_TOKEN = "authorizationToken"

    init {
        sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE)
    }

    fun saveSessionToken(token: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(SESSION_TOKEN, token)
        TokenContainer.updateSessionToken(token)
        editor.apply()
    }

    fun saveAuthorizationToken(token: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(AUTHORIZATION_TOKEN, token)
        TokenContainer.updateAuthorizationToken(token)
        editor.apply()
    }

    fun sessionToken(): String? {
        return sharedPreferences.getString(SESSION_TOKEN, null)
    }

    fun authorizationToken(): String? {
        return sharedPreferences.getString(AUTHORIZATION_TOKEN, null)
    }
}