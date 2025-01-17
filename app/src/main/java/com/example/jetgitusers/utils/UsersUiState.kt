package com.example.jetgitusers.utils

sealed interface UsersUiState {
    object Loading : UsersUiState
    object Success : UsersUiState
    object Error : UsersUiState
}