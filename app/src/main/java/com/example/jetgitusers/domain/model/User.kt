package com.example.jetgitusers.domain.model

data class User (
    val login: String,
    val id: Int,
    val avatar_url: String,
    val url: String,
    val followers_url: String,
    val public_repos: Int,
    val followers: Int
)