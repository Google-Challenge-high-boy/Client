package com.highboy.gomantle.network

import com.highboy.gomantle.data.User
import retrofit2.http.GET

interface GomantleApiService {
    @GET("photos")
    suspend fun getUsers(): List<User>
}