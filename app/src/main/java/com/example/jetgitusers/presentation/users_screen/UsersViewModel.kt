package com.example.jetgitusers.presentation.users_screen

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
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    var usersUiState: UsersUiState by mutableStateOf(UsersUiState.Loading)
        private set

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    fun getUsers(token: String) {
        viewModelScope.launch {
            usersUiState = UsersUiState.Loading
            try {
                val usersList = repository.getUsers(token)
                _users.value = usersList
                usersUiState = UsersUiState.Success
            } catch (e: HttpException) {
                usersUiState = UsersUiState.Error
            }
        }
    }
}