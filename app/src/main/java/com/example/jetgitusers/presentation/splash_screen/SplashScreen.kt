package com.example.jetgitusers.presentation.splash_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.jetgitusers.reusable_components.GithubCard
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel = hiltViewModel(),
    navigate: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1000)
        navigate(viewModel.hasToken())
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        GithubCard(32, 3, Modifier.fillMaxSize())
    }
}
