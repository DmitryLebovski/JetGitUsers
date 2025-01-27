package com.example.jetgitusers.domain

sealed class UsersIntent {
    object LoadUsers : UsersIntent()
    object LoadMoreUsers : UsersIntent()
}