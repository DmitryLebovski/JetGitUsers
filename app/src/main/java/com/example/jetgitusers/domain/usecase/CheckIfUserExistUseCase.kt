package com.example.jetgitusers.domain.usecase

import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.UserRepository
import javax.inject.Inject

class CheckIfUserExistUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(token: String): Result<User> {
        return userRepository.checkIfUserExist(token = token)
    }
}