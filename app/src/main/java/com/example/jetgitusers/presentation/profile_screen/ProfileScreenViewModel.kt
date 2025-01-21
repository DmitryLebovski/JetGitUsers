package com.example.jetgitusers.presentation.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.TokenRepository
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.AppError
import com.example.jetgitusers.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
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
            getToken().collect { token ->
                token?.let {
                    _uiState.value = UiState.Loading
                    try {
                        val userInfo = repository.getAuthorizedUser(token)
                        _user.value = userInfo
                        _uiState.value  = UiState.Success
                    } catch (e: IOException) {
                        _uiState.value  = UiState.Error(AppError.SYSTEM)
                    } catch (e: HttpException) {
                        _uiState.value  = UiState.Error(AppError.INTERNET)
                    }
                }
            }
        }
    }

    fun getToken() = tokenRepository.getToken()
    suspend fun clearToken() = tokenRepository.clearToken()
}