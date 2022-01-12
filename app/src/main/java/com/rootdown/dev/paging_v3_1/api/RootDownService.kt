package com.rootdown.dev.paging_v3_1.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val IN_QUALIFIER = ""

/**
 * RootDown.Green API communication setup via Retrofit.
 */
interface RootDownService {
    /**
     * Get repo of profiles list.
     */
    @GET("api/profilels?")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): RepoSearchResponse

    @GET("api/latlng")
    suspend fun getLatLng(): NetworkLatLngContainer

    companion object {
        private const val BASE_URL = "https://redfeatherbotanicals.com/"

        fun create(): RootDownService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RootDownService::class.java)
        }
    }
}