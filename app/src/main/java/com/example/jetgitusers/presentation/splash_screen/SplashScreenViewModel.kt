package com.example.jetgitusers.presentation.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.usecase.GetAuthUserUseCase
import com.example.jetgitusers.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val getAuthUserUseCase: GetAuthUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }

            getAuthUserUseCase()
                .onFailure { throwable -> _uiState.update { UiState.Error(throwable) }}
                .onSuccess { _uiState.update { UiState.Success }}
        }
    }
}