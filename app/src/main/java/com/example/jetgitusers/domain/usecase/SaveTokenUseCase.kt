package com.example.jetgitusers.domain.usecase

import com.example.jetgitusers.domain.repository.TokenRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(token: String) {
        return tokenRepository.saveToken(token)
    }
}