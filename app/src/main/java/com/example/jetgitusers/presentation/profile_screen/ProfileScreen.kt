package com.example.jetgitusers.presentation.profile_screen

import androidx.compose.foundation.Image
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
import com.example.jetgitusers.R
import com.example.jetgitusers.data.remote.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable

fun ProfileScreen(
    navigate: () -> Unit
) {
    val context = LocalContext.current

    val i = 0 // TODO
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.light_grey)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.github_placeholder),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.username_placeholder),
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(R.font.montserrat_regular),
            )
        )
        Text(
            text = stringResource(R.string.followers_placeholder, i),
            fontFamily = FontFamily(Font(R.font.montserrat_regular))
        )
        Text(
            text = stringResource(R.string.repositories_placeholder, i),
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