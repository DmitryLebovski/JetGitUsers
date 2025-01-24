package com.example.jetgitusers.presentation.followers_screen

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
class FollowersScreenViewModel @Inject constructor(
    private val repository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _followers = MutableStateFlow<List<User>>(emptyList())
    val followers: StateFlow<List<User>> = _followers

    fun getUserFollowers(page: Int, username: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.getUserFollowers(
                username = username,
                page = page
            )

            if (result.isSuccess) {
                result.getOrNull()?.let { userList ->
                    Log.d("FOLLOWERS_ID", userList.map { it.id }.toString())

                    val updatedList = mutableListOf<User>()
                    for (user in userList) {
                        val detailsResult = repository.getUserInfo(user.login)
                        if (detailsResult.isSuccess) {
                            detailsResult.getOrNull()?.let { details ->
                                updatedList.add(user.copy(followers = details.followers))
                            }
                        } else {
                            val error = detailsResult.exceptionOrNull()
                            Log.d("exeptFollows", error.toString())
                            _uiState.emit(UiState.Error(error ?: Exception("Неизвестная ошибка")))
                            return@launch
                        }
                    }

                    _followers.emit(_followers.value + updatedList)
                    _uiState.emit(UiState.Success)
                }
            } else {
                val error = result.exceptionOrNull()
                Log.d("exeptFollows", error.toString())
                _uiState.emit(UiState.Error(error ?: Exception("Неизвестная ошибка")))
            }
        }
    }


    suspend fun clearToken() = tokenRepository.clearToken()
}