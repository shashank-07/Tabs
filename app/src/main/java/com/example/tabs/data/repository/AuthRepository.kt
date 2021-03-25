package com.example.tabs.data.repository

import com.example.tabs.data.UserPreferences
import com.example.tabs.data.network.AuthApi


class AuthRepository(
        private val api: AuthApi,
        private val preferences: UserPreferences
) : BaseRepository(){

    suspend fun login(
            email: String,
            password: String
    ) = safeApiCall {
        api.login(email, password)
    }
    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }



}