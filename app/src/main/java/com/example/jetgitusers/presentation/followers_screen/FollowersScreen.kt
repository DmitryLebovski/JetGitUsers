package com.example.jetgitusers.presentation.followers_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetgitusers.R
import com.example.jetgitusers.presentation.login_screen.ErrorScreen
import com.example.jetgitusers.reusable_components.ShimmerUserList
import com.example.jetgitusers.reusable_components.UserCard
import com.example.jetgitusers.utils.CheckConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowersScreen(
    username: String,
    navigateIfError: () -> Unit,
    navigateToFollowers: (String) -> Unit,
    popBackStack: () -> Unit,
    viewModel: FollowersScreenViewModel = hiltViewModel(),
){
    val followersPagingItems = viewModel.getFollowers(username).collectAsLazyPagingItems()
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    var isRefreshingState by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
            PullToRefreshBox(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                state = refreshState,
                isRefreshing = isRefreshingState,
                onRefresh = {
                    isRefreshingState = true
                    coroutineScope.launch {
                        delay(200)
                        followersPagingItems.refresh() //RETRY отрабатывает только при state ERROR
                        isRefreshingState = false
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(R.color.light_grey)),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    LazyColumn(
                        modifier = Modifier
                            .background(color = colorResource(R.color.light_grey))
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        state = lazyListState
                    ) {
                        items(followersPagingItems.itemCount) { index ->
                            val user = followersPagingItems[index]
                            user?.let {
                                UserCard(
                                    login = user.login,
                                    avatarUrl = user.avatar_url,
                                    followers = user.followers,
                                    onClick = {
                                        navigateToFollowers(user.login)
                                    }
                                )
                            }
                        }
                    }
                    followersPagingItems.apply {
                        when (loadState.append) {
                            is LoadState.Loading -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
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

                            is LoadState.Error -> {
                                val error = followersPagingItems.loadState.append as LoadState.Error
                                Row(modifier = Modifier.padding(top = 4.dp)) {
                                    Text(
                                        text = stringResource(R.string.connection_failed),
                                        modifier = Modifier.padding(16.dp)
                                    )

                                    Spacer(modifier = Modifier.padding(start = 16.dp))

                                    Button(
                                        onClick = { followersPagingItems.retry() }
                                    ) {
                                        Text(stringResource(R.string.retry))
                                    }
                                }
                                Toast.makeText(
                                    context,
                                    error.error.localizedMessage,
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }

                            is LoadState.NotLoading -> {}
                        }
                    }
                }

            }

            when (followersPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    ShimmerUserList(modifier = Modifier
                        .padding(top = paddingValues.calculateTopPadding()))
                }
                is LoadState.Error -> {
                    if (!CheckConnection.isInternetAvailable(context)) {
                        ErrorScreen(
                            reload = { followersPagingItems.retry() },
                            logOut = { navigateIfError() }
                        )
                        Toast.makeText(context, stringResource(R.string.token_error), Toast.LENGTH_LONG).show()
                    } else {
                        followersPagingItems.retry()
                    }
                }
                else -> {}
            }
        }
    )
}
