package com.highboy.gomantle.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    companion object {
        // network
        private val BASE_URL = "https://47fa3e21-8401-4b7e-910a-6dd2da2d6ea0.mock.pstmn.io"

        private val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val retrofitService: GomantleApiService by lazy {
            retrofit.create(GomantleApiService::class.java)
        }
    }
}