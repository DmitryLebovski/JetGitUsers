package com.example.jetgitusers.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.jetgitusers.domain.model.User

sealed interface UiState {
    object Loading : UiState
    object Success : UiState
    class Error(val error: AppError) : UiState
}

enum class AppError {
    SYSTEM, INTERNET
}

object CheckConnection {
    fun isInternetAvailable(context: Context): Boolean {
        val result: Boolean

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        result = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
        return result
    }
}

sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<User>, val loadMore: Boolean = false) : UsersState()
    data class Error(val error: AppError) : UsersState()
}
