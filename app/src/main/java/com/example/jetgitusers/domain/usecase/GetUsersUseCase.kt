package com.example.jetgitusers.domain.usecase

import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(since: Int): Result<List<User>> {
        return userRepository.getUsers(since = since)
    }
}