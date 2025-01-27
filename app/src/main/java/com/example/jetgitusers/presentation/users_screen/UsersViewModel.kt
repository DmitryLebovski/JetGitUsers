package com.example.jetgitusers.presentation.users_screen

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

            repository.getUsers(since = 0)
                .onFailure { throwable -> _uiState.update { UsersState.Error(throwable) }}
                .onSuccess { userList ->
                    _uiState.update {
                        UsersState.Success(users = userList, loadMore = false)
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
                _uiState.update { currentState.copy(loadMore = true) }

                repository.getUsers(lastUserId)
                    .onFailure { _uiState.emit(UsersState.Error(it)) }
                    .onSuccess { userList ->
                        _uiState.update {
                            UsersState.Success(users = currentState.users + userList, loadMore = false)
                        }
                    }
            }
        }
    }

    suspend fun clearToken() = tokenRepository.clearToken()
}