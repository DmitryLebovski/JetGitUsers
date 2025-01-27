package com.example.jetgitusers.presentation.users_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.UsersIntent
import com.example.jetgitusers.domain.usecase.ClearTokenUseCase
import com.example.jetgitusers.domain.usecase.GetUsersUseCase
import com.example.jetgitusers.utils.UsersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel

class UsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val clearTokenUseCase: ClearTokenUseCase
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

            getUsersUseCase(since = 0)
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

                getUsersUseCase(lastUserId)
                    .onFailure { _uiState.emit(UsersState.Error(it)) }
                    .onSuccess { userList ->
                        _uiState.update {
                            UsersState.Success(users = currentState.users + userList, loadMore = false)
                        }
                    }
            }
        }
    }

    suspend fun clearToken() = clearTokenUseCase()
}