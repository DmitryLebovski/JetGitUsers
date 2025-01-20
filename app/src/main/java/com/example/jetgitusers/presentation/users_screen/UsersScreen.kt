package com.example.jetgitusers.presentation.users_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetgitusers.R
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.presentation.login_screen.ErrorScreen
import com.example.jetgitusers.presentation.login_screen.LoadingScreen
import com.example.jetgitusers.reusable_components.UserCard
import com.example.jetgitusers.utils.UsersUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun UsersScreen(
    viewModel: UsersViewModel = hiltViewModel()
){
    val token = viewModel.token.collectAsState(initial = null)
    val uiState = viewModel.usersUiState
    val usersList by viewModel.users.collectAsState()

    LaunchedEffect(key1 = token.value) {
        delay(100L)
        viewModel.getUsers(token.value.toString())
    }

    when(uiState) {
        UsersUiState.Success -> {
            UserListScreen(usersList)
        }

        UsersUiState.Loading -> {
            LoadingScreen()
        }

        UsersUiState.Error -> {
            ErrorScreen()
        }
    }
}

@Composable
fun UserListScreen(
    usersList: List<User>
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.light_grey))
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(usersList) { user ->
            UserCard(
                login = user.login,
                avatarUrl = user.avatar_url,
                followers = 0
            )
        }
    }
}
