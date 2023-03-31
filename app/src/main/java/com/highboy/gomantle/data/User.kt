package com.highboy.gomantle.data

import kotlinx.serialization.SerialName

data class User (
    val userId: Long,
    val email: String,
    val userName: String
)