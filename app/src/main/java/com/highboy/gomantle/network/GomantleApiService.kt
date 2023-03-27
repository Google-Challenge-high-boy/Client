package com.highboy.gomantle.network

import com.highboy.gomantle.GlobalConstants
import com.highboy.gomantle.data.User
import retrofit2.http.*

interface GomantleApiService {

    @Headers("userId: ${GlobalConstants.USER_EMAIL}")
    @POST("gomantle/wrong")
    suspend fun getSimilarity(
        @Body body: GetSimilarityRequest
    ): GetSimilarityResponse
}