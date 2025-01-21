package com.example.jetgitusers.presentation.followers_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.TokenRepository
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.AppError.INTERNET
import com.example.jetgitusers.utils.AppError.SYSTEM
import com.example.jetgitusers.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
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

    val token = tokenRepository.getToken()

    fun getUserFollowers(token: String, page: Int, username: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
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
                _uiState.value = UiState.Success
            } catch (e: IOException) {
                _uiState.value = UiState.Error(SYSTEM)
            } catch (e: HttpException) {
                _uiState.value = UiState.Error(INTERNET)
            }
        }
    }

    suspend fun clearToken() = tokenRepository.clearToken()
}