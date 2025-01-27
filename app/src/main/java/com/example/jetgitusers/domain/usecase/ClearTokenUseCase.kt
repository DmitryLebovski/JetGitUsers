package com.example.jetgitusers.domain.usecase

import com.example.jetgitusers.domain.repository.TokenRepository
import javax.inject.Inject

class ClearTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke() {
        return tokenRepository.clearToken()
    }
}