package com.example.jetgitusers.domain.repository

interface TokenRepository {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}