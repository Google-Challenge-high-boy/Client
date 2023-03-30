package com.highboy.gomantle.network

data class GetRankResponse (
    val myRank: Int,
    val allRank: List<String>
)