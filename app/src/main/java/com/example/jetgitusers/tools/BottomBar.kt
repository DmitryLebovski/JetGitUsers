package com.example.jetgitusers.tools

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.jetgitusers.R
import com.example.jetgitusers.ui.theme.AppFont
import com.example.jetgitusers.utils.Routes.PROFILE_SCREEN
import com.example.jetgitusers.utils.Routes.USERS_SCREEN

@Composable
fun BottomBar(
    currentRoute: String?,
    navigateToUsers:() -> Unit,
    navigateToProfile: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = {
                Text(
                    text = stringResource(R.string.users_icon),
                    fontFamily = AppFont.AwesomeFont,
                    fontWeight = FontWeight.SemiBold
                )
            },
            label = { Text(stringResource(R.string.users_button)) },
            selected = currentRoute == USERS_SCREEN,
            onClick = { navigateToUsers() },
            alwaysShowLabel = false,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
            )
        )
        NavigationBarItem(
            icon = {
                Text(
                    text = stringResource(R.string.activity_icon),
                    fontFamily = AppFont.AwesomeFont,
                    fontWeight = FontWeight.SemiBold
                )
            },
            label = { Text(stringResource(R.string.extra_task_button)) },
            selected = false,
            enabled = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
            )
        )
        NavigationBarItem(
            icon = {
                Text(
                    text = stringResource(R.string.account_icon),
                    fontFamily = AppFont.AwesomeFont,
                    fontWeight = FontWeight.SemiBold
                )
            },
            label = { Text(stringResource(R.string.profile_button)) },
            selected = currentRoute == PROFILE_SCREEN,
            onClick = { navigateToProfile() },
            alwaysShowLabel = false,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
            )
        )
    }
}
