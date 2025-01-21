package com.example.jetgitusers.utils

sealed interface UiState {
    object Loading : UiState
    object Success : UiState
    class Error(val error: AppError) : UiState
}

enum class AppError {
    SYSTEM, INTERNET
}