package com.example.jetgitusers.domain.repository

import com.example.jetgitusers.domain.model.User

interface UserRepository {
    suspend fun getAuthorizedUser(token: String): User
}