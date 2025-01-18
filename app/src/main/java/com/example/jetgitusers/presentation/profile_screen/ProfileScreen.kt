package com.example.jetgitusers.presentation.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.jetgitusers.R
import com.example.jetgitusers.data.DataStoreManager
import com.example.jetgitusers.presentation.login_screen.LoadingScreen
import com.example.jetgitusers.utils.UsersUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    token: String,
    navigate: () -> Unit
) {
    val context = LocalContext.current
    val user by viewModel.user.collectAsState()
    val uiState = viewModel.usersUiState

    LaunchedEffect(key1 = token){
        viewModel.getUserData(token)
    }

    when(uiState) {
        UsersUiState.Success -> {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(R.color.light_grey)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AsyncImage(
                    model = user.avatar_url,
                    placeholder = painterResource(R.drawable.github_placeholder),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user.login,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular),
                    )
                )
                Text(
                    text = stringResource(R.string.followers_placeholder, user.followers),
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
                Text(
                    text = stringResource(R.string.repositories_placeholder, user.public_repos),
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick =  {
                        CoroutineScope(Dispatchers.IO).launch {
                            DataStoreManager.clearToken(context)
                        }
                        navigate()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.log_out),
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Red
                    )
                }
            }
        }
        UsersUiState.Loading -> LoadingScreen()
        UsersUiState.Error -> LoadingScreen()
    }
}