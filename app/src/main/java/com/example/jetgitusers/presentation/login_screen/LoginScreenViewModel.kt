package com.example.jetgitusers.presentation.login_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
class LoginScreenViewModel @Inject constructor(
    private val repository: UserRepository,
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

    fun checkUserExist(token: String) {
        viewModelScope.launch {
            try {
                usersUiState = UsersUiState.Loading
                val userInfo = repository.getAuthorizedUser(token)
                Log.d("API_RESPONSE", userInfo.toString())
                _user.emit(userInfo)
                usersUiState = UsersUiState.Success
                repository.saveToken(token)
            } catch (e: HttpException) {
                usersUiState = UsersUiState.Error
            } catch (e: IOException) {
                usersUiState = UsersUiState.Error
            } catch (e: IllegalArgumentException) {
                usersUiState = UsersUiState.Error
            }
        }
    }
}