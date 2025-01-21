package com.example.jetgitusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetgitusers.presentation.login_screen.LoginScreen
import com.example.jetgitusers.presentation.main_screens.MainScreens
import com.example.jetgitusers.presentation.splash_screen.SplashScreen
import com.example.jetgitusers.ui.theme.JetGitUsersTheme
import com.example.jetgitusers.utils.Routes.LOGIN_SCREEN
import com.example.jetgitusers.utils.Routes.MAIN_ROUTE
import com.example.jetgitusers.utils.Routes.SPLASH_SCREEN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb()))
        setContent {
            JetGitUsersTheme {
                Surface(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize()
                ) {
                    // TODO START AppNavigation ----------------------------------------------------
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
                            enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb()))
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
                            MainScreens(
                                parentNavController = navController,
                                enableEdgeToEdge = { systemBarStyle ->
                                    enableEdgeToEdge(systemBarStyle)
                                }
                            )
                        }
                    }
                    // TODO END AppNavigation ------------------------------------------------------
                }
            }
        }
    }
}

//enum class ROUTES {
//
//}
//
//@Composable
//fun AppNav() {
//    NavHost(...) {
//        ....
//    }
//}

