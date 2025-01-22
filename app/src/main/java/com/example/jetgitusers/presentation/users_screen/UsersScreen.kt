package com.example.jetgitusers.presentation.users_screen

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetgitusers.R
import com.example.jetgitusers.domain.UsersIntent
import com.example.jetgitusers.presentation.login_screen.ErrorScreen
import com.example.jetgitusers.presentation.login_screen.LoadingScreen
import com.example.jetgitusers.reusable_components.UserCard
import com.example.jetgitusers.utils.AppError
import com.example.jetgitusers.utils.CheckConnection
import com.example.jetgitusers.utils.Routes.FOLLOWERS_SCREEN
import com.example.jetgitusers.utils.UsersState

@Composable
fun UsersScreen(
    navigateIfError: () -> Unit,
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    // исходная ошибка lazyListState.layoutInfo:  Frequently changing state(The object of LazyListLayoutInfo
    // calculated during the last layout pass) should not be directly read in composable function
    // любое изменение lazyListState приведет к изменению переменной

    // решение: by remember { derivedStateOf { lazyListState.layoutInfo } }
    // оптимизирован для отслеживания изменений, предотвращая ненужные персчеты,
    // если знчение изменилось, но не затронуло логику
    // (значение будет переписано исключительно в случае реального изменения lazyListState.layoutInfo)


    val layoutInfo by remember { derivedStateOf { lazyListState.layoutInfo } }
    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    val itemCount = layoutInfo.totalItemsCount

    LaunchedEffect(Unit) {
        viewModel.processIntent(UsersIntent.LoadUsers)
    }

    val isLoadMore = (uiState as? UsersState.Success)?.loadMore == true

    when (uiState) {
        is UsersState.Loading -> LoadingScreen()
        is UsersState.Success -> {
            val usersList = (uiState as UsersState.Success).users
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
                    if (isLoadMore && CheckConnection.isInternetAvailable(context)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (!CheckConnection.isInternetAvailable(context)) {
                        Text(
                            text = stringResource(R.string.connection_failed),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
        is UsersState.Error -> {
            Log.d("exeptUs", (uiState as UsersState.Error).error.toString())
            if ((uiState as UsersState.Error).error == AppError.INTERNET) {
                Toast.makeText(context, stringResource(R.string.token_error), Toast.LENGTH_LONG).show()
                LaunchedEffect(Unit) {
                    viewModel.clearToken()
                    navigateIfError()
                }
            }

            if ((uiState as UsersState.Error).error == AppError.SYSTEM) {
                if (!CheckConnection.isInternetAvailable(context)){
                    ErrorScreen()
                } else {
                    viewModel.processIntent(UsersIntent.LoadUsers)
                }
            }
        }
    }

    LaunchedEffect(key1 = lastVisibleIndex) {
        if (lastVisibleIndex == itemCount - 1 && !isLoadMore) {
            viewModel.processIntent(UsersIntent.LoadMoreUsers)
        }
    }
}
