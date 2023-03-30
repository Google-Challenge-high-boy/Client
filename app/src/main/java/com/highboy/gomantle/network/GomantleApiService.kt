package com.highboy.gomantle.network

import com.highboy.gomantle.GlobalConstants
import com.highboy.gomantle.data.User
import retrofit2.http.*

interface GomantleApiService {

    // Log in
    @POST("v1/users/signin")
    suspend fun signIn(
        @Body body: SignInRequest
    ): SignInResponse

    @POST("v1/users/signup")
    suspend fun signUp(
        @Body body: SignUpRequest
    ): SignUpResponse

    // Game

    @POST("gomantle/wrong")
    suspend fun getSimilarity(
        @Body body: GetSimilarityRequest
    ): GetSimilarityResponse

    @GET("gomantle")
    suspend fun getAnswerList(): GiveUpResponse

    // friend

    @POST("friends")
    suspend fun followUp(
        @HeaderMap headerMap: Map<String, Long>,
        @Body body: FollowUpRequest
    ): FollowUpResponse

    @GET("friends")
    suspend fun getFriends(
        @HeaderMap headerMap: Map<String, Long>,
    ): List<GetFriendsResponse>

    // rank

    // my-page
    @Headers("userId: ${GlobalConstants.USER_EMAIL}")
    @GET("v1/rank")
    suspend fun getRank(
        @Body body: GetRankRequest
    ): GetRankResponse



    // account
}