package com.example.jetgitusers.domain.usecase

import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.repository.UserRepository
import javax.inject.Inject

class GetUserFollowersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String, page: Int): Result<List<User>> {
        return userRepository.getUserFollowers(username = username, page = page)
    }
}