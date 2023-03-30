package com.highboy.gomantle.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    companion object {
        // network
        private val BASE_URL = "http://10.0.2.2:8080/"

        private val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val retrofitService: GomantleApiService by lazy {
            retrofit.create(GomantleApiService::class.java)
        }
    }
}