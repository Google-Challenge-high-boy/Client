package com.highboy.gomantle.data

import androidx.compose.material3.rememberTopAppBarState
import com.highboy.gomantle.network.GomantleApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.create

interface AppContainer {
    val gomantleRepository: GomantleRepository
}

class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://7da93587-aa16-435b-954a-5be7d8ccaeb1.mock.pstmn.io/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: GomantleApiService by lazy {
        retrofit.create(GomantleApiService::class.java)
    }

    override val gomantleRepository: GomantleRepository by lazy {
        NetworkGomantleRepository(retrofitService)
    }
}