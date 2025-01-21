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
import androidx.compose.runtime.derivedStateOf
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
import com.example.jetgitusers.presentation.login_screen.ErrorScreen
import com.example.jetgitusers.presentation.login_screen.LoadingScreen
import com.example.jetgitusers.reusable_components.UserCard
import com.example.jetgitusers.utils.AppError
import com.example.jetgitusers.utils.Routes.FOLLOWERS_SCREEN
import com.example.jetgitusers.utils.UiState

@Composable
fun UsersScreen(
    navigateIfError: () -> Unit,
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel(),
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val usersList by viewModel.users.collectAsState()

    var isLoading by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    val layoutInfo by remember { derivedStateOf { lazyListState.layoutInfo } }
    // исходная ошибка lazyListState.layoutInfo:  Frequently changing state(The object of LazyListLayoutInfo
    // calculated during the last layout pass) should not be directly read in composable function
    // любое изменение lazyListState приведет к изменению переменной

    // решение: by remember { derivedStateOf { lazyListState.layoutInfo } }
    // оптимизирован для отслеживания изменений, предотвращая ненужные персчеты,
    // если знчение изменилось, но не затронуло логику
    // (значение будет переписано исключительно в случае реального изменения lazyListState.layoutInfo)
    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    val itemCount = layoutInfo.totalItemsCount

    if (uiState == UiState.Loading && usersList.isEmpty()) {
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
                if (uiState == UiState.Loading) {
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
        if (lastVisibleIndex == itemCount - 1 && uiState != UiState.Loading) {
            isLoading = true
            val lastUserId = usersList.lastOrNull()?.id

            viewModel.getUsers(since = lastUserId?: 1)
            isLoading = false
        }
    }

    if (uiState is UiState.Error && (uiState as UiState.Error).error == AppError.INTERNET) {
        Toast.makeText(context, stringResource(R.string.token_error), Toast.LENGTH_LONG)
            .show()
        LaunchedEffect(Unit){
            viewModel.clearToken()
            navigateIfError()
        }
    }

    if (uiState is UiState.Error && (uiState as UiState.Error).error == AppError.SYSTEM) {
        ErrorScreen(
            update = { viewModel.getUsers(1) }
        )
    }
}
