package com.highboy.gomantle.network

data class GetFriendsResponse(
    val followee: Followee,
    val follower: Follower,
    val id: Long
)

data class Followee(
    val email: String,
    val id: Long,
    val name: String
)

data class Follower(
    val email: String,
    val id: Long,
    val name: String
)