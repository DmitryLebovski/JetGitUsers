package com.example.jetgitusers.presentation.main_screens

import androidx.activity.SystemBarStyle
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetgitusers.R
import com.example.jetgitusers.presentation.profile_screen.ProfileScreen
import com.example.jetgitusers.presentation.users_screen.UsersScreen
import com.example.jetgitusers.tools.BottomBar
import com.example.jetgitusers.utils.Routes.LOGIN_SCREEN
import com.example.jetgitusers.utils.Routes.PROFILE_SCREEN
import com.example.jetgitusers.utils.Routes.USERS_SCREEN

@Composable
fun MainScreens(
    enableEdgeToEdge: (SystemBarStyle) -> Unit,
    parentNavController: NavController
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = USERS_SCREEN,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(USERS_SCREEN) {
                enableEdgeToEdge(
                    SystemBarStyle.light(
                        getColor(context, R.color.light_grey),
                        getColor(context, R.color.white)
                    )
                )
                UsersScreen(
                    navigate = {
                        parentNavController.navigate(LOGIN_SCREEN) {
                            popUpTo(USERS_SCREEN) { inclusive = true }
                        }
                    }
                )
            }

            composable(PROFILE_SCREEN) {
                ProfileScreen(
                    navigate = {
                        parentNavController.navigate(LOGIN_SCREEN) {
                            popUpTo(PROFILE_SCREEN) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
