package com.example.jetgitusers.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jetgitusers.domain.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor

val Context.dataStore by preferencesDataStore("settings")
object DataStoreManager {
    val TOKEN_KEY = stringPreferencesKey("github_token")

    fun customAuthInterceptor(tokenRepository: TokenRepository): Interceptor {
        return Interceptor { chain ->
            val token = runBlocking {
                tokenRepository.getToken()
            }
            val requestBuilder = chain.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(requestBuilder.build())
        }
    }
}

