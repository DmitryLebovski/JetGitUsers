package com.example.jetgitusers.presentation.followers_screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetgitusers.R
import com.example.jetgitusers.presentation.login_screen.ErrorScreen
import com.example.jetgitusers.reusable_components.ShimmerUserList
import com.example.jetgitusers.reusable_components.UserCard
import com.example.jetgitusers.utils.AppError
import com.example.jetgitusers.utils.UiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowersScreen(
    username: String,
    navigateIfError: () -> Unit,
    navigateToFollowers: (String) -> Unit,
    popBackStack: () -> Unit,
    viewModel: FollowersScreenViewModel = hiltViewModel(),
){
    
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val followersList by viewModel.followers.collectAsState()

    var page by remember { mutableIntStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100L)
        viewModel.getUserFollowers(
            page = page,
            username = username
        )
    }

    val lazyListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val layoutInfo by remember { derivedStateOf { lazyListState.layoutInfo } }
    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    val itemCount = layoutInfo.totalItemsCount

    if (uiState == UiState.Loading && followersList.isEmpty()) {
        ShimmerUserList()
    } else {
        Scaffold (
            topBar =  {
                TopAppBar(
                    title = {
                        Text(text = username)
                    },
                    navigationIcon = {
                        IconButton(onClick = { popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.light_grey),
                        scrolledContainerColor = colorResource(R.color.light_grey)
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(R.color.light_grey))
                        .padding(top = paddingValues.calculateTopPadding() + 8.dp)
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    state = lazyListState
                ) {
                    items(followersList) { user ->
                        UserCard(
                            login = user.login,
                            avatarUrl = user.avatar_url,
                            followers = user.followers,
                            onClick = {
                                navigateToFollowers(user.login)
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
        )
    }

    LaunchedEffect(key1 = lastVisibleIndex) {
        if (lastVisibleIndex == itemCount - 1 && uiState != UiState.Loading) {
            isLoading = true
            page += 1

            viewModel.getUserFollowers(
                page = page,
                username = username
            )
            isLoading = false
        }
    }

    if (uiState is UiState.Error) {
        val error = (uiState as UiState.Error).error

        when (error) {
            is AppError.Internet -> {
                Toast.makeText(context, stringResource(R.string.token_error), Toast.LENGTH_LONG)
                    .show()
                LaunchedEffect(Unit){
                    viewModel.clearToken()
                }
                navigateIfError()
            }

            is AppError.System -> {
                ErrorScreen()
            }
        }
    }
}
