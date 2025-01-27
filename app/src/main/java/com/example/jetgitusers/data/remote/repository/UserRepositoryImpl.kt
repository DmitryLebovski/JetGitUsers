package com.example.jetgitusers.data.remote.repository

import com.example.jetgitusers.data.remote.UserApi
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.AppError

class UserRepositoryImpl(
    private val api: UserApi
): UserRepository {

    override suspend fun checkIfUserExist(token: String): Result<User> {
        return try {
            val response = api.checkIsUserExist(authorization = "Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = AppError.Internet(
                    httpCode = response.code(),
                    httpMessage = response.message()
                )
                Result.failure(error)
            }
        } catch (e: Exception) {
            Result.failure(AppError.System())
        }
    }

    override suspend fun getAuthorizedUser(): Result<User> {
        return try {
            val response = api.getAuthenticatedUser()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = AppError.Internet(
                    httpCode = response.code(),
                    httpMessage = response.message()
                )
                Result.failure(error)            }
        } catch (e: Exception) {
            Result.failure(AppError.System())
        }
    }

    override suspend fun getUsers(since: Int): Result<List<User>> {
        return try {
            val response = api.getUsers(sinceId = since)
            if (response.isSuccessful) {
                val users = response.body()?.let { mapDtoToUsers(it) } ?: emptyList()
                val detailedUsers = users.map { user ->
                    getUserInfo(user.login).getOrNull()?.let {
                        user.copy(followers = it.followers)
                    } ?: user
                }
                Result.success(detailedUsers)
            } else {
                val error = AppError.Internet(
                    httpCode = response.code(),
                    httpMessage = response.message()
                )
                Result.failure(error)              }
        } catch (e: Exception) {
            Result.failure(AppError.System())
        }
    }

    override suspend fun getUserFollowers(username: String, page: Int): Result<List<User>> {
        return try {
            val response = api.getUserFollowers(username = username, page = page)
            if (response.isSuccessful) {
                val users = response.body()?.let { mapDtoToUsers(it) } ?: emptyList()
                val detailedUsers = users.map { user ->
                    getUserInfo(user.login).getOrNull()?.let {
                        user.copy(followers = it.followers)
                    } ?: user
                }

                Result.success(detailedUsers)
            } else {
                val error = AppError.Internet(
                    httpCode = response.code(),
                    httpMessage = response.message()
                )
                Result.failure(error)
            }
        } catch (e: Exception) {
            Result.failure(AppError.System())
        }
    }

    override suspend fun getUserInfo(username: String): Result<User> {
        return try {
            val response = api.getUserInfo(username = username)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = AppError.Internet(
                    httpCode = response.code(),
                    httpMessage = response.message()
                )
                Result.failure(error)
            }
        } catch (e: Exception) {
            Result.failure(AppError.System())
        }
    }

    private fun mapDtoToUsers(dtoList: List<User>): List<User> {
        return dtoList.map { dto ->
            User(
                login = dto.login,
                id = dto.id,
                avatar_url = dto.avatar_url,
                url = dto.url,
                followers_url = dto.followers_url,
                public_repos = 0,
                followers = 0
            )
        }
    }
}