package com.example.jetgitusers.presentation.login_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetgitusers.R
import com.example.jetgitusers.reusable_components.GithubCard

@Composable

fun LoginScreen(
    navigate: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    var elementsPadding = Modifier.fillMaxWidth().padding(horizontal = 16.dp)

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(24.dp))

        GithubCard(16, 2, Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.padding(24.dp))

        Column {
            Text(
                text = stringResource(R.string.token),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                modifier = elementsPadding
            )

            Spacer(modifier = Modifier.padding(4.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                placeholder = { Text(
                    text = stringResource(R.string.token_placeholder),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular),
                    )
                ) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.dark_grey)
                ),
                modifier = elementsPadding
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = {
                    navigate()
                    text = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.dark_grey)
                ),
                modifier = elementsPadding
            ) {
                Text(
                    text = "Sign In",
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            }

        }
    }
}