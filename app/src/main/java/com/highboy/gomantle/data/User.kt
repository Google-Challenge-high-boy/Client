package com.highboy.gomantle.data

import kotlinx.serialization.SerialName

data class User (
    @SerialName(value = "email")
    val email: String,
    @SerialName(value = "username")
    val userName: String
) : java.io.Serializable