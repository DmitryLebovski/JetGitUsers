package com.example.jetgitusers.presentation.users_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.UsersIntent
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.TokenRepository
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.AppError.SYSTEM
import com.example.jetgitusers.utils.AppError.INTERNET
import com.example.jetgitusers.utils.UsersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject@HiltViewModel

class UsersViewModel @Inject constructor(
    private val repository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UsersState>(UsersState.Loading)
    val uiState: StateFlow<UsersState> = _uiState

    private val _users = MutableStateFlow<List<User>>(emptyList())

    val token = tokenRepository.getToken()
    private var isLoadingMore = false

    fun processIntent(intent: UsersIntent) {
        when (intent) {
            is UsersIntent.LoadUsers -> loadUsers()
            is UsersIntent.LoadMoreUsers -> loadMoreUsers()
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            token.collect { collectedToken ->
                collectedToken?.let {
                    _uiState.value = UsersState.Loading
                    try {
                        val usersList = repository.getUsers(collectedToken, 1)
                        val detailedUsers = usersList.map { user ->
                            val detailedUser = repository.getUserInfo(user.login, collectedToken)
                            user.copy(followers = detailedUser.followers)
                        }
                        _users.value += detailedUsers
                        _uiState.value = UsersState.Success(users = _users.value, loadMore = false)
                    } catch (e: IOException) {
                        Log.d("exeptUs", e.toString())
                        _uiState.value = UsersState.Error(SYSTEM)
                    } catch (e: HttpException) {
                        Log.d("exeptUs", e.toString())
                        _uiState.value = UsersState.Error(INTERNET)
                    }
                }
            }
        }
    }

    private fun loadMoreUsers() {
        val currentState = _uiState.value
        if (currentState !is UsersState.Success) return
        else {
            val lastUserId = currentState.users.lastOrNull()?.id ?: return
            viewModelScope.launch {
                try {
                    _uiState.value = currentState.copy(loadMore = true)
                    val usersList = repository.getUsers(tokenRepository.getToken().first()!!, lastUserId)
                    val detailedUsers = usersList.map { user ->
                        val detailedUser = repository.getUserInfo(user.login, tokenRepository.getToken().first()!!)
                        user.copy(followers = detailedUser.followers)
                    }
                    _users.value += detailedUsers
                    _uiState.value = UsersState.Success(users = _users.value, loadMore = false)
                } catch (e: IOException) {
                    Log.d("exeptUs", e.toString())
                    _uiState.value = UsersState.Error(SYSTEM)
                } catch (e: HttpException) {
                    Log.d("exeptUs", e.toString())
                    _uiState.value = UsersState.Error(INTERNET)
                } finally {
                    isLoadingMore = false
                }
            }
        }
    }

    suspend fun clearToken() = tokenRepository.clearToken()
}