package com.example.jetgitusers.presentation.users_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.jetgitusers.R
import com.example.jetgitusers.reusable_components.GithubCard
import com.example.jetgitusers.reusable_components.UserCard

@Composable
fun UsersScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.light_grey))
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        repeat(10) { index ->
           UserCard()
        }
    }
}
