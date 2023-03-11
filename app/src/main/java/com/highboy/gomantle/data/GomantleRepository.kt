package com.highboy.gomantle.data

import com.highboy.gomantle.network.GomantleApiService

interface GomantleRepository {
    suspend fun getUsers(): List<User>
}

class NetworkGomantleRepository(
    private val gomantleApiService: GomantleApiService
) : GomantleRepository {
    override suspend fun getUsers(): List<User> = gomantleApiService.getUsers()
}