package com.example.jetgitusers.presentation.splash_screen

import androidx.lifecycle.ViewModel
import com.example.jetgitusers.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    fun getToken() = repository.getToken()
}