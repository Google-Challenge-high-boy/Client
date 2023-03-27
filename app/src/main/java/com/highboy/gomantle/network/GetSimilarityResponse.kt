package com.highboy.gomantle.network

data class GetSimilarityResponse (
    val result: Int,
    val similarity: Float,
    val answerList: Map<String, String>
)