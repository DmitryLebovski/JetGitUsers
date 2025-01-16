package com.example.jetgitusers.presentation.splash_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

import com.example.jetgitusers.reusable_components.GithubCard
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigate: () -> Unit
) {
    LaunchedEffect(true) {
        delay(1000)
        navigate()
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        GithubCard(32, 3, Modifier.fillMaxSize())
    }
}
