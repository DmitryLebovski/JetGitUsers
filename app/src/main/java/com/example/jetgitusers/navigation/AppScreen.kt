package com.example.jetgitusers.navigation

import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetgitusers.presentation.login_screen.LoginScreen
import com.example.jetgitusers.presentation.splash_screen.SplashScreen
import com.example.jetgitusers.utils.Routes.LOGIN_SCREEN
import com.example.jetgitusers.utils.Routes.MAIN_ROUTE
import com.example.jetgitusers.utils.Routes.SPLASH_SCREEN

@Composable
fun AppScreen(
    enableEdgeToEdge: (SystemBarStyle) -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        composable(SPLASH_SCREEN) {
            SplashScreen(
                navigate = { hasToken ->
                    if (hasToken) {
                        navController.navigate(MAIN_ROUTE) {
                            popUpTo(SPLASH_SCREEN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(LOGIN_SCREEN) {
                            popUpTo(SPLASH_SCREEN) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(LOGIN_SCREEN) {
            BackHandler(true) {}
            enableEdgeToEdge(
                SystemBarStyle.light(
                    Color.Transparent.toArgb(),
                    Color.Transparent.toArgb()
                )
            )
            LoginScreen(
                navigate = {
                    navController.navigate(MAIN_ROUTE) {
                        popUpTo(MAIN_ROUTE) { inclusive = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }

        composable(MAIN_ROUTE) {
            MainScreen(
                navigateToLogin = {
                    navController.navigate(LOGIN_SCREEN) {
                        popUpTo(MAIN_ROUTE) { inclusive = true }
                    }
                },
                enableEdgeToEdge = { systemBarStyle ->
                    enableEdgeToEdge(systemBarStyle)
                }
            )
        }
    }
}