package com.example.jetgitusers.data.remote

import com.example.jetgitusers.domain.model.User
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {
    @GET("/user")
    suspend fun getAuthenticatedUser(
        @Header("Authorization") authorization: String,
    ): User
}