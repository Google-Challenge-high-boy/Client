package com.highboy.gomantle.network

data class GetSimilarityResponse (
    val similarity: Float,
    val answerList: Map<String, String>
)