package com.example.jetgitusers.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    val TOKEN_KEY = stringPreferencesKey("github_token")
    val system_error = "Нет интернета или системная ошибка"
}