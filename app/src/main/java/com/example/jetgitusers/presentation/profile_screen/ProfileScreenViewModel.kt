package com.example.jetgitusers.presentation.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.usecase.ClearTokenUseCase
import com.example.jetgitusers.domain.usecase.GetAuthUserUseCase
import com.example.jetgitusers.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val getAuthUserUseCase: GetAuthUserUseCase,
    private val clearTokenUseCase: ClearTokenUseCase
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

    init {
        getUserData()
    }

    fun getUserData() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            getAuthUserUseCase()
                .onFailure { throwable -> _uiState.update { UiState.Error(throwable) }}
                .onSuccess {
                    _user.emit(it)
                    _uiState.update { UiState.Success }
                }
        }
    }

    suspend fun clearToken() = clearTokenUseCase()
}