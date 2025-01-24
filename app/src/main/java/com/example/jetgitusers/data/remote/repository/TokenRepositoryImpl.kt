package com.example.jetgitusers.data.remote.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.jetgitusers.domain.repository.TokenRepository
import com.example.jetgitusers.utils.Constants.TOKEN_KEY
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class TokenRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): TokenRepository {

    override suspend fun getToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.firstOrNull()
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