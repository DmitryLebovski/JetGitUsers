package com.example.jetgitusers.presentation.splash_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.jetgitusers.data.remote.DataStoreManager

import com.example.jetgitusers.reusable_components.GithubCard
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigate: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val tokenFlow = DataStoreManager.getToken(context).collectAsState(initial = null)

    LaunchedEffect(tokenFlow.value) {
        delay(1000)
        if (tokenFlow.value != null) {
            navigate(true)
        } else navigate(false)
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        GithubCard(32, 3, Modifier.fillMaxSize())
    }
}
