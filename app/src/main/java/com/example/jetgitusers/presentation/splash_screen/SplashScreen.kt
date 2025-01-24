package com.example.jetgitusers.presentation.splash_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetgitusers.reusable_components.GithubCard
import com.example.jetgitusers.utils.UiState

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel = hiltViewModel(),
    navigate: (Boolean) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UiState.Error -> { LaunchedEffect(Unit) { navigate(false) } }
        UiState.Loading -> {
            Column (
                modifier = Modifier.fillMaxSize()
            ) {
                GithubCard(32, 3, Modifier.fillMaxSize())
            }
        }
        UiState.Success -> { LaunchedEffect(Unit) { navigate(true) } }
    }
}
