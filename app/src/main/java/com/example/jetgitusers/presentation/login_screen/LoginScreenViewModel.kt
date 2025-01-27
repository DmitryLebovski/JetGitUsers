package com.example.jetgitusers.presentation.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.usecase.CheckIfUserExistUseCase
import com.example.jetgitusers.domain.usecase.SaveTokenUseCase
import com.example.jetgitusers.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val checkIfUserExistUseCase: CheckIfUserExistUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _user = MutableStateFlow(
        User(
            login = "def",
            id = 0,
            avatar_url = "def",
            url = "def",
            followers_url = "def",
            public_repos = 0,
            followers = 0,
        )
    )

    val user: StateFlow<User> = _user

    fun checkUserExist(token: String) {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            checkIfUserExistUseCase(token)
                .onFailure { throwable -> _uiState.update { UiState.Error(throwable) } }
                .onSuccess {
                    _user.update { it }
                    _uiState.update { UiState.Success }
                }
        }
    }

    suspend fun saveToken(token: String) = saveTokenUseCase(token)
}