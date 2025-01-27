package com.example.jetgitusers.data.remote

import com.example.jetgitusers.domain.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("/user")
    suspend fun checkIsUserExist(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/vnd.github+json"
    ): Response<User>

    @GET("/user")
    suspend fun getAuthenticatedUser(
        @Header("Accept") accept: String = "application/vnd.github+json"
    ): Response<User>

    @GET("/users")
    suspend fun getUsers(
        @Header("Accept") accept: String = "application/vnd.github+json",
        @Query("per_page") perPage: Int = 30,
        @Query("since") sinceId: Int = 1
    ): Response<List<User>>

    @GET("/users/{username}")
    suspend fun getUserInfo(
        @Header("Accept") accept: String = "application/vnd.github+json",
        @Path("username") username: String
    ): Response<User>

    @GET("/users/{username}/followers")
    suspend fun getUserFollowers(
        @Header("Accept") accept: String = "application/vnd.github+json",
        @Path("username") username: String,
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): Response<List<User>>
}