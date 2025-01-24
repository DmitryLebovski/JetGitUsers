package com.example.jetgitusers.presentation.users_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.UsersIntent
import com.example.jetgitusers.domain.repository.TokenRepository
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.UsersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel

class UsersViewModel @Inject constructor(
    private val repository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UsersState>(UsersState.Loading)
    val uiState: StateFlow<UsersState> = _uiState

    fun processIntent(intent: UsersIntent) {
        when (intent) {
            is UsersIntent.LoadUsers -> loadUsers()
            is UsersIntent.LoadMoreUsers -> loadMoreUsers()
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { UsersState.Loading }

            repository.getUsers(since = 1)
                .onFailure { _uiState.emit(UsersState.Error(it)) }
                .onSuccess { userList ->
                    val detailedUsers = userList.map { user ->
                        repository.getUserInfo(user.login).getOrNull()?.let {
                            user.copy(followers = it.followers)
                        } ?: user
                    }

                    _uiState.update { UsersState.Success(users = detailedUsers, loadMore = false) }
                }
        }
    }

    private fun loadMoreUsers() {
        val currentState = _uiState.value
        if (currentState !is UsersState.Success) return
        else {
            val lastUserId = currentState.users.lastOrNull()?.id ?: return
            viewModelScope.launch {
                _uiState.value = currentState.copy(loadMore = true)
                val result = repository.getUsers(lastUserId)

                if (result.isSuccess) {
                    result.getOrNull()?.let { userList ->
                        val detailedUsers = userList.mapNotNull { user ->
                            val detailedUserResult = repository.getUserInfo(user.login)
                            if (detailedUserResult.isSuccess) {
                                detailedUserResult.getOrNull()?.let { details ->
                                    user.copy(followers = details.followers)
                                }
                            } else {
                                val error = detailedUserResult.exceptionOrNull()
                                Log.d("exeptFollows", error.toString())
                                _uiState.emit(UsersState.Error(error ?: Exception("Неизвестная ошибка")))
                                return@launch
                            }
                        }

                        _uiState.emit(UsersState.Success(users = currentState.users + detailedUsers, loadMore = false))
                    }
                } else {
                    val error = result.exceptionOrNull()
                    Log.d("exeptFollows", error.toString())
                    _uiState.emit(UsersState.Error(error ?: Exception("Неизвестная ошибка")))
                }
            }
        }
    }

    suspend fun clearToken() = tokenRepository.clearToken()
}