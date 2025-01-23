package com.example.jetgitusers.presentation.splash_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val token: String?
) : ViewModel() {
    fun hasToken(): Boolean = !token.isNullOrEmpty()
}