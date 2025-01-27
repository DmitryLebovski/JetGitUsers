package com.example.jetgitusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.jetgitusers.navigation.AppScreen
import com.example.jetgitusers.ui.theme.JetGitUsersTheme
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
                    AppScreen(
                        enableEdgeToEdge = { systemBarStyle ->
                            enableEdgeToEdge(systemBarStyle)
                        }
                    )
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

