package com.example.jetgitusers.presentation.login_screen

import android.util.Log
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
class LoginScreenViewModel @Inject constructor(
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

    fun checkUserExist(token: String) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val userInfo = repository.getAuthorizedUser(token)
                _user.emit(userInfo)
                _uiState.value = UiState.Success
            } catch (e: HttpException) {
                Log.d("exeptLogin", e.toString())
                _uiState.value = UiState.Error(AppError.INTERNET)
            } catch (e: IOException) {
                Log.d("exeptLogin", e.toString())
                _uiState.value = UiState.Error(AppError.SYSTEM)
            } catch (e: IllegalArgumentException) {
                Log.d("exeptLogin", e.toString())
                _uiState.value = UiState.Error(AppError.SYSTEM)
            }
        }
    }

    suspend fun saveToken(token: String) = tokenRepository.saveToken(token)
}