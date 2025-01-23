package com.example.jetgitusers.domain.repository

import com.example.jetgitusers.domain.model.User

interface UserRepository {
    suspend fun checkIfUserExist(token: String): User
    suspend fun getAuthorizedUser(): User
    suspend fun getUsers(since: Int): List<User>
    suspend fun getUserFollowers(username: String, page: Int): List<User>
    suspend fun getUserInfo(username: String): User
}