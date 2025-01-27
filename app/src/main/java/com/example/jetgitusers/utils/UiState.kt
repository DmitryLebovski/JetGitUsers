package com.example.jetgitusers.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.utils.Constants.system_error

sealed interface UiState {
    object Loading : UiState
    object Success : UiState
    class Error(val error: Throwable) : UiState
}

sealed class AppError(message: String): Throwable(message) {
    class System : AppError(system_error)
    class Internet(val httpCode: Int, httpMessage: String) : AppError(httpMessage)
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
    data class Error(val error: Throwable) : UsersState()
}
