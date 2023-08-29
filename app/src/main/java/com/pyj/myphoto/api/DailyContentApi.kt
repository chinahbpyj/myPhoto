package com.pyj.myphoto.api

import com.pyj.myphoto.data.DailyContentData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface DailyContentApi {
    companion object {
        const val BASE_URL = "https://apier.youngam.cn/essay/one"

        fun createPxApi(): DailyContentApi {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl("https://apier.youngam.cn/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DailyContentApi::class.java)
        }
    }

    @GET
    suspend fun daily(
        @Url url: String
    ): DailyContentData
}
