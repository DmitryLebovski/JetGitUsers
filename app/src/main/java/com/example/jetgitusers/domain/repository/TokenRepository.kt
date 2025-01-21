package com.example.jetgitusers.domain.repository

import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getToken(): Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}