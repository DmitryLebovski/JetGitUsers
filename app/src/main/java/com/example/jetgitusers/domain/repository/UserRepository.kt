package com.example.jetgitusers.domain.repository

import com.example.jetgitusers.domain.model.User

interface UserRepository {
    suspend fun checkIfUserExist(token: String): Result<User>
    suspend fun getAuthorizedUser(): Result<User>
    suspend fun getUsers(since: Int): Result<List<User>>
    suspend fun getUserFollowers(username: String, page: Int): Result<List<User>>
    suspend fun getUserInfo(username: String): Result<User>
}