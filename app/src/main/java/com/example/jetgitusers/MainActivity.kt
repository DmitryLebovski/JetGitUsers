package com.example.jetgitusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetgitusers.tools.BottomBar
import com.example.jetgitusers.ui.theme.JetGitUsersTheme
import androidx.navigation.compose.composable
import com.example.jetgitusers.data.DataStoreManager
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.presentation.login_screen.LoginScreen
import com.example.jetgitusers.presentation.profile_screen.ProfileScreen
import com.example.jetgitusers.presentation.splash_screen.SplashScreen
import com.example.jetgitusers.presentation.users_screen.UsersScreen
import com.example.jetgitusers.utils.Routes.LOGIN_SCREEN
import com.example.jetgitusers.utils.Routes.PROFILE_SCREEN
import com.example.jetgitusers.utils.Routes.SPLASH_SCREEN
import com.example.jetgitusers.utils.Routes.USERS_SCREEN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb()))
        setContent {
            val token by DataStoreManager.getToken(applicationContext).collectAsState(initial = null)

            JetGitUsersTheme {
                Surface (
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val currentBackStackEntry = navController.currentBackStackEntryAsState()

                    Scaffold (
                        bottomBar = {
                            val currentRoute = currentBackStackEntry.value?.destination?.route
                            if (currentRoute != SPLASH_SCREEN && currentRoute != LOGIN_SCREEN) {
                                BottomBar(navController)
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = SPLASH_SCREEN,
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable(SPLASH_SCREEN) {
                                SplashScreen (
                                    navigate = { hasToken ->
                                        if (hasToken) {
                                            navController.navigate(USERS_SCREEN) {
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
                                enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb()))
                                LoginScreen(
                                    navigate = {
                                        navController.navigate(USERS_SCREEN)
                                    }
                                )
                            }

                            composable(USERS_SCREEN) {
                                enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(getColor(R.color.light_grey), getColor(R.color.white)))
                                UsersScreen()
                            }

                            composable(PROFILE_SCREEN) {
                                ProfileScreen(
                                    token = token.toString(),
                                    navigate = {
                                        navController.navigate(LOGIN_SCREEN) {
                                            popUpTo(0) { inclusive = true }
                                            launchSingleTop = true
                                            restoreState = false
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}