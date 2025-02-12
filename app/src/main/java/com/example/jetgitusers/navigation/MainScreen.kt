package com.example.jetgitusers.navigation

import androidx.activity.SystemBarStyle
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetgitusers.R
import com.example.jetgitusers.presentation.followers_screen.FollowersScreen
import com.example.jetgitusers.presentation.profile_screen.ProfileScreen
import com.example.jetgitusers.presentation.users_screen.UsersScreen
import com.example.jetgitusers.tools.BottomBar
import com.example.jetgitusers.utils.Routes.FOLLOWERS_SCREEN
import com.example.jetgitusers.utils.Routes.PROFILE_SCREEN
import com.example.jetgitusers.utils.Routes.USERS_SCREEN
import kotlinx.coroutines.flow.map

@Composable
fun MainScreen(
    enableEdgeToEdge: (SystemBarStyle) -> Unit,
    navigateToLogin: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val currentRoute by navController.currentBackStackEntryFlow
        .map { it.destination.route }
        .collectAsState(initial = USERS_SCREEN)

    Scaffold(
        bottomBar = { BottomBar(
            currentRoute = currentRoute,
            navigateToUsers = {
                navController.navigate(USERS_SCREEN)
            },
            navigateToProfile = {
                navController.navigate(PROFILE_SCREEN)
            }
        ) }
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
                    navigateIfError = navigateToLogin,
                    navController
                )
            }

            composable(
                route = "$FOLLOWERS_SCREEN/{username}",
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: return@composable
                enableEdgeToEdge(
                    SystemBarStyle.light(
                        getColor(context, R.color.light_grey),
                        getColor(context, R.color.white)
                    )
                )
                FollowersScreen(
                    username = username,
                    navigateIfError = navigateToLogin,
                    navigateToFollowers = { nextUser ->
                        navController.navigate("$FOLLOWERS_SCREEN/$nextUser")
                    },
                    popBackStack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(PROFILE_SCREEN) {
                ProfileScreen(
                    navigateIfError = navigateToLogin,
                )
            }
        }
    }
}
