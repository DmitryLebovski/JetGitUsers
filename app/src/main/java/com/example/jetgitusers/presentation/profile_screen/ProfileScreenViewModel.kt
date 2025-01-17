package com.example.jetgitusers.presentation.profile_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.UsersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var usersUiState: UsersUiState by mutableStateOf(UsersUiState.Loading)
        private set

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

    fun getUserData(token: String) {
        viewModelScope.launch {
            usersUiState = UsersUiState.Loading
            try {
                val userInfo = repository.getAuthorizedUser(token)
                _user.value = userInfo
                usersUiState = UsersUiState.Success
            } catch (e: IOException) {
                usersUiState = UsersUiState.Error
            } catch (e: HttpException) {
                usersUiState = UsersUiState.Error
            }
        }
    }
}