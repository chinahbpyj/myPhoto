package com.pyj.myphoto.api

import com.pyj.myphoto.data.PxPhoto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PxApi {
    companion object {
        private const val BASE_URL = "http://500px.me/community/discover/"
        fun createPxApi(): PxApi {
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
                .create(PxApi::class.java)
        }
    }

    @GET("{ty}")
    suspend fun curated(
        @Path("ty") ty: String?,
        @Query("page") page: String?,
        @Query("size") size: String?,
        @Query("type") type: String?
    ): List<PxPhoto>
}
