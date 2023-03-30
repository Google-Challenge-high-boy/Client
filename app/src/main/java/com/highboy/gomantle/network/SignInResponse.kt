package com.highboy.gomantle.network

data class SignInResponse (
    val status: Int,
    val date: String?,
    val User: User?
)

data class User(
    val userid: Long,
    val name: String,
    val email: String
)