package com.example.jetgitusers.presentation.splash_screen

import androidx.lifecycle.ViewModel
import com.example.jetgitusers.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {
    fun getToken() = tokenRepository.getToken()
}