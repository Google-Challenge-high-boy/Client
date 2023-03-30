package com.highboy.gomantle.network

data class SignInResponse (
    val status: Int,
    val user: User?,
    val date: String?
)

data class User(
    val id: Long,
    val email: String,
    val name: String
)