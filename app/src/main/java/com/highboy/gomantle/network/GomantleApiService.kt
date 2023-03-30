package com.highboy.gomantle.network

import com.highboy.gomantle.GlobalConstants
import com.highboy.gomantle.data.User
import retrofit2.http.*

interface GomantleApiService {

    // game

    @Headers("userId: ${GlobalConstants.USER_EMAIL}")
    @POST("gomantle/wrong")
    suspend fun getSimilarity(
        @Body body: GetSimilarityRequest
    ): GetSimilarityResponse

    @GET("gomantle")
    suspend fun getAnswerList(): GiveUpResponse

    // friend

//    @Headers("userId: ${GlobalConstants.USER_EMAIL}")
//    @POST("friends")
//    suspend fun followUp(
//        @Body body: FollowUpRequest
//    ): FollowUpResponse
//
//    @Headers("userId: ${GlobalConstants.USER_EMAIL}")
//    @GET("friends")
//    suspend fun getFriendInfo(
//    ): GetFriendInfoResponse

    // rank

    // my-page
    @Headers("userId: ${GlobalConstants.USER_EMAIL}")
    @GET("v1/rank")
    suspend fun getRank(
        @Body body: GetRankRequest
    ): GetRankResponse



    // account
}