package com.example.tabs.data.network

import com.example.tabs.data.responses.ClusterResponse
import com.example.tabs.data.responses.ItenaryResponse
import com.example.tabs.data.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    // Add your api calls here

    @FormUrlEncoded

    @POST("auth/login")
    suspend fun login(
        @Field("email") email :String,
        @Field("password") password :String,
    ) : LoginResponse

    @FormUrlEncoded

    @POST("auth/signup")
    suspend fun signup(
        @Field("name") name :String,
        @Field("email") email :String,
        @Field("password") password :String,
        @Field("places_preference") places_preference :List<String>
    ) : LoginResponse

    @FormUrlEncoded

    @POST("api/get_user_cluster")
    suspend fun getCluster(
        @Field("latitude") latitude :Double,
        @Field("longitude") longitude :Double
    ) : ClusterResponse

    @FormUrlEncoded

    @POST("api/get_itenary")
    suspend fun getItenary(
        @Field("budget") budget :Int,
        @Field("time") time :Int,
        @Field("refresh") refresh :Int,
        @Field("cluster") cluster :Int
    ) : ItenaryResponse



}


