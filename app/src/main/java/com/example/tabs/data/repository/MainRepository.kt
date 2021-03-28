package com.example.tabs.data.repository

import com.example.tabs.data.UserPreferences
import com.example.tabs.data.network.Api


class MainRepository(
    private val api: Api,
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
    suspend fun saveCluster(cluster:Int){
        preferences.saveCluster(cluster)
    }


    suspend fun signup(
        name: String,
        email: String,
        password: String,
        places_preference: List<String>
    ) = safeApiCall {
        api.signup(name,email,password,places_preference)
    }

    suspend fun getCluster(
        latitude: Double,
        longitude: Double,
        ) = safeApiCall {
        api.getCluster(latitude,longitude)
    }

    suspend fun getItenary(
        budget: Int,
        time: Int,
        refresh:Int,
        cluster:Int,
        places_pref:String
    ) = safeApiCall {
        api.getItenary(budget, time, refresh, cluster,places_pref)
    }



}


