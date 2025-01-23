package com.example.jetgitusers.data.remote.repository

import com.example.jetgitusers.data.remote.UserApi
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi
): UserRepository {

    override suspend fun checkIfUserExist(token: String): User {
        return api.checkIsUserExist(authorization = "Bearer $token")
    }

    override suspend fun getAuthorizedUser(): User {
        return api.getAuthenticatedUser()
    }

    override suspend fun getUsers(since: Int): List<User> {
        return api.getUsers(sinceId = since).map { dto->
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

    override suspend fun getUserFollowers(username: String, page: Int): List<User> {
        return api.getUserFollowers(username = username, page = page).map { dto->
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

    override suspend fun getUserInfo(username: String): User {
        return api.getUserInfo(username = username)
    }
}