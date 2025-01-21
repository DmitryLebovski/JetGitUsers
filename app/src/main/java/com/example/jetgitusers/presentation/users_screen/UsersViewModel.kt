package com.example.jetgitusers.presentation.users_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.TokenRepository
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.AppError.SYSTEM
import com.example.jetgitusers.utils.AppError.INTERNET
import com.example.jetgitusers.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// TODO MVI

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    val token = tokenRepository.getToken()

    init {
        getUsers(1)
    }

    fun getUsers(since: Int) {
        viewModelScope.launch {
            token.collect { collectedToken ->
                collectedToken?.let {
                    _uiState.value = UiState.Loading
                    try {
                        val usersList = repository.getUsers(collectedToken, since)
                        Log.d("USER_ID", usersList.map { it.id }.toString())


                        val updatedList = usersList.map { user ->
                            val detailedUser = repository.getUserInfo(user.login, collectedToken)
                            user.copy(followers = detailedUser.followers)
                        }

                        _users.value += updatedList
                        _uiState.value = UiState.Success
                    } catch (e: IOException) {
                        Log.d("exeptUs", e.toString())
                        _uiState.value = UiState.Error(SYSTEM)
                    } catch (e: HttpException) {
                        Log.d("exeptUs", e.toString())
                        _uiState.value = UiState.Error(INTERNET)
                    }
                }
            }
        }
    }

    suspend fun clearToken() = tokenRepository.clearToken()
}