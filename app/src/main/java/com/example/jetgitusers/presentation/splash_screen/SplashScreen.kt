package com.example.jetgitusers.presentation.splash_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.jetgitusers.reusable_components.GithubCard
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel = hiltViewModel(),
    navigate: (Boolean) -> Unit
) {
    val token = viewModel.getToken().collectAsState(initial = null)

    LaunchedEffect(token.value) {
        delay(1000)
        if (token.value != null) {
            navigate(true)
        } else navigate(false)
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        GithubCard(32, 3, Modifier.fillMaxSize())
    }
}
