package com.pyj.myphoto.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YouxkApi {
    companion object {
        private const val BASE_URL = "http://www.youxiake.net/"

        fun createPxApi(): YouxkApi {
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
                .create(YouxkApi::class.java)
        }
    }

    @GET("{type}")
    fun dataList(
        @Path("type") type: String,
        @Query("f") f: String
    ): Call<ResponseBody>
}
