package com.example.jetgitusers.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("settings")
object DataStoreManager { val TOKEN_KEY = stringPreferencesKey("github_token") }

