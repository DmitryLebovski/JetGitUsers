package com.example.jetgitusers.domain.repository

import com.example.jetgitusers.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getAuthorizedUser(token: String): User
    suspend fun getUsers(token: String): List<User>
    fun getToken(): Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}