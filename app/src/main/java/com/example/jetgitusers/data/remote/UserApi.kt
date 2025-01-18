package com.example.jetgitusers.data.remote

import com.example.jetgitusers.domain.model.User
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UserApi {
    @GET("/user")
    suspend fun getAuthenticatedUser(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/vnd.github+json"
    ): User

    @GET("/users")
    suspend fun getUsers(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/vnd.github+json",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): List<User>

    @GET("/users/{username}/followers")
    suspend fun getUserFollowers(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/vnd.github+json"
    ): User
}