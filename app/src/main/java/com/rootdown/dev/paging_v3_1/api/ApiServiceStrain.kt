package com.rootdown.dev.paging_v3_1.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ApiServiceStrain {

    @GET("api/strainls")
    suspend fun getStrainsList(): NetworkStrainContainer

    companion object {
        private const val BASE_URL = "https://visionarymatrixs.com/"

        fun create(): ApiServiceStrain {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ApiServiceStrain::class.java)
        }
    }
}