package com.example.jetgitusers.data.remote.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.jetgitusers.data.DataStoreManager.TOKEN_KEY
import com.example.jetgitusers.data.remote.UserApi
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//  TODO слишком много делает репозиторий S из SOLID
class UserRepositoryImpl(
    private val api: UserApi,
    private val dataStore: DataStore<Preferences>
): UserRepository {
    override suspend fun getAuthorizedUser(token: String): User {
        return api.getAuthenticatedUser(authorization = "Bearer $token")
    }

    override suspend fun getUsers(token: String, since: Int): List<User> {
        return api.getUsers(authorization = "Bearer $token", sinceId = since).map { dto->
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

    override suspend fun getUserFollowers(username: String, page: Int, token: String): List<User> {
        return api.getUserFollowers(username = username, page = page, authorization = "Bearer $token").map { dto->
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

    override suspend fun getUserInfo(username: String, token: String): User {
        return api.getUserInfo(username = username, authorization = "Bearer $token")
    }

    override fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}