package com.example.jetgitusers.presentation.followers_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.usecase.ClearTokenUseCase
import com.example.jetgitusers.domain.usecase.GetUserFollowersUseCase
import com.example.jetgitusers.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersScreenViewModel @Inject constructor(
    private val getUserFollowersUseCase: GetUserFollowersUseCase,
    private val clearTokenUseCase: ClearTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _followers = MutableStateFlow<List<User>>(emptyList())
    val followers: StateFlow<List<User>> = _followers

    fun getUserFollowers(page: Int, username: String) {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            getUserFollowersUseCase(
                username = username,
                page = page
            )
                .onFailure { throwable -> _uiState.update { UiState.Error(throwable) } }
                .onSuccess { followerList ->
                    Log.d("FOLLOWERS_ID", followerList.map { it.id }.toString())
                    _followers.update { _followers.value + followerList }
                    _uiState.update { UiState.Success }
                }
        }
    }


    suspend fun clearToken() = clearTokenUseCase()
}