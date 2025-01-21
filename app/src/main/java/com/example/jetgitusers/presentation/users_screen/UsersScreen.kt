package com.example.jetgitusers.presentation.users_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetgitusers.R
import com.example.jetgitusers.presentation.login_screen.LoadingScreen
import com.example.jetgitusers.reusable_components.UserCard
import com.example.jetgitusers.utils.Routes.FOLLOWERS_SCREEN
import com.example.jetgitusers.utils.UsersUiState
import kotlinx.coroutines.delay

@Composable
fun UsersScreen(
    navigateIfError: () -> Unit,
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel(),
){
    val context = LocalContext.current
    val token = viewModel.token.collectAsState(initial = null)
    val uiState = viewModel.usersUiState
    val usersList by viewModel.users.collectAsState()

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = token.value) {
        delay(100L)
        viewModel.getUsers(token.value.toString(), 1)
    }

    val lazyListState = rememberLazyListState()

    val layoutInfo = lazyListState.layoutInfo
    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    val itemCount = layoutInfo.totalItemsCount

    if (uiState == UsersUiState.Loading && usersList.isEmpty()) {
        LoadingScreen()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.light_grey))
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = lazyListState
        ) {
            items(usersList) { user ->
                UserCard(
                    login = user.login,
                    avatarUrl = user.avatar_url,
                    followers = user.followers,
                    onClick = {
                        navController.navigate("$FOLLOWERS_SCREEN/${user.login}")
                    }
                )
            }

            item {
                if (uiState == UsersUiState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = lastVisibleIndex) {
        if (lastVisibleIndex == itemCount - 1 && uiState != UsersUiState.Loading) {
            isLoading = true
            val lastUserId = usersList.lastOrNull()?.id

            viewModel.getUsers(token = token.value.toString(), since = lastUserId?: 1)
            isLoading = false
        }
    }

    if (uiState == UsersUiState.Error) {
        Toast.makeText(context, stringResource(R.string.token_error), Toast.LENGTH_LONG)
            .show()
        LaunchedEffect(Unit){
            viewModel.clearToken()
        }
        navigateIfError()
    }
}
