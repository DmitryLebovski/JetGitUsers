package com.example.jetgitusers.presentation.users_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetgitusers.R
import com.example.jetgitusers.presentation.login_screen.ErrorScreen
import com.example.jetgitusers.reusable_components.ShimmerUserList
import com.example.jetgitusers.reusable_components.UserCard
import com.example.jetgitusers.utils.CheckConnection
import com.example.jetgitusers.utils.Routes.FOLLOWERS_SCREEN

@Composable
fun UsersScreen(
    navigateIfError: () -> Unit,
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val usersPagingItems = viewModel.usersPagingFlow.collectAsLazyPagingItems() //получаем через api
    // для отображения данных, статусов загрузки и тд

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.light_grey)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(usersPagingItems.itemCount) { index ->
            val user = usersPagingItems[index]
            user?.let {
                UserCard(
                    login = it.login,
                    avatarUrl = it.avatar_url,
                    followers = it.followers,
                    onClick = {
                        navController.navigate("$FOLLOWERS_SCREEN/${it.login}")
                    }
                )
            }
        }

        usersPagingItems.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.more_users_loading),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(end = 8.dp)
                            )
                        }
                    }
                }

                is LoadState.Error -> {
                    val error = usersPagingItems.loadState.append as LoadState.Error
                    item {
                        Row {
                            Text(
                                text = stringResource(R.string.connection_failed),
                                modifier = Modifier.padding(16.dp)
                            )

                            Spacer(modifier = Modifier.padding(start = 16.dp))

                            Button(
                                onClick = { usersPagingItems.retry() }
                            ) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                    Toast.makeText(context, error.error.localizedMessage, Toast.LENGTH_LONG).show()
                }

                is LoadState.NotLoading -> {}
            }
        }
    }

    when (usersPagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            ShimmerUserList()
        }
        is LoadState.Error -> {
            if (!CheckConnection.isInternetAvailable(context)) {
                ErrorScreen(
                    reload = { usersPagingItems.retry() },
                    logOut = { navigateIfError() }
                )
                Toast.makeText(context, stringResource(R.string.token_error), Toast.LENGTH_LONG).show()
            } else {
                usersPagingItems.retry()
            }
        }
        else -> {  }
    }
}
