package com.highboy.gomantle.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User (
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "img_src")
    val userName: String
) : java.io.Serializable