package com.example.jetgitusers.data.remote.repository

import com.example.jetgitusers.data.remote.UserApi
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi
): UserRepository {
    override suspend fun getAuthorizedUser(token: String): User {
        return api.getAuthenticatedUser(authorization = "Bearer $token")
    }
}