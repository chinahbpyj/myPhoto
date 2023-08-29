package com.pyj.myphoto.api

import com.pyj.myphoto.data.DailyData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface DailyApi {
    companion object {
        const val BASE_URL = "http://open.iciba.com/dsapi/"

        fun createPxApi(): DailyApi {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DailyApi::class.java)
        }
    }

    @GET
    suspend fun daily(
        @Url url: String
    ): DailyData
}
