package com.example.jetgitusers.presentation.followers_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.TokenRepository
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.UsersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class FollowersScreenViewModel @Inject constructor(
    private val repository: UserRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {
    var usersUiState: UsersUiState by mutableStateOf(UsersUiState.Loading)
        private set

    private val _followers = MutableStateFlow<List<User>>(emptyList())
    val followers: StateFlow<List<User>> = _followers

    val token = tokenRepository.getToken()

    fun getUserFollowers(token: String, page: Int, username: String) {
        viewModelScope.launch {
            usersUiState = UsersUiState.Loading
            try {
                val followersList = repository.getUserFollowers(
                    username = username,
                    page = page,
                    token = token
                )
                Log.d("FOLLOWERS_ID", followersList.map { it.id }.toString())


                val updatedList = followersList.map { user ->
                    val detailedFollower = repository.getUserInfo(user.login, token)
                    user.copy(followers = detailedFollower.followers)
                }

                _followers.value += updatedList
                usersUiState = UsersUiState.Success
            } catch (e: HttpException) {
                usersUiState = UsersUiState.Error
            }
        }
    }

    suspend fun clearToken() = tokenRepository.clearToken()
}