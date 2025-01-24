package com.example.jetgitusers.presentation.profile_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.TokenRepository
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val repository: UserRepository,
    private val tokenRepository: TokenRepository
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
            _uiState.value = UiState.Loading
            val result = repository.getAuthorizedUser()
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _user.value = it
                    _uiState.emit(UiState.Success)
                }
            } else {
                val error = result.exceptionOrNull()
                Log.d("exeptUs", error.toString())
                _uiState.emit(UiState.Error(error ?: Exception("Неизвестная ошибка")))
            }
        }
    }

    suspend fun clearToken() = tokenRepository.clearToken()
}