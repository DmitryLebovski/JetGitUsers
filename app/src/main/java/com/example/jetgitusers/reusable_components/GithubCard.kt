package com.example.jetgitusers.reusable_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetgitusers.R
import com.example.jetgitusers.ui.theme.AppFont

@Composable
fun GithubCard(
    multiplierIcon: Int,
    multiplierText: Int,
    modifier: Modifier
    ) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(R.string.github_emoji),
            fontSize = (multiplierIcon * 8).sp,
            fontFamily = AppFont.AwesomeFont,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(R.string.bellerage_github),
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontWeight = FontWeight.Bold,
            fontSize = (multiplierText * 8).sp
        )
    }
}