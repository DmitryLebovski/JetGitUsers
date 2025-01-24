package com.example.jetgitusers.presentation.splash_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetgitusers.domain.repository.UserRepository
import com.example.jetgitusers.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = userRepository.getAuthorizedUser()
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _uiState.emit(UiState.Success)
                }
            } else {
                val error = result.exceptionOrNull()
                Log.d("exeptUs", error.toString())
                _uiState.emit(UiState.Error(error ?: Exception("Неизвестная ошибка")))
            }
        }
    }
}