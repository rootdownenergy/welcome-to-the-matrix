package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net

import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.CharacterSearchResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCharacterExplorer {
    //call the get method for characters
    @GET("api/characters")
    suspend fun getCharacters(
        @Query("name") query: String,
        @Query("offset") page: Int,
        @Query("limit") itemsPerPage: Int
    ) : List<BreakingBadChar>
    //ini
    companion object {
        const val BASE_URL = "https://breakingbadapi.com/"
        fun ini(): ApiCharacterExplorer {
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
                .create(ApiCharacterExplorer::class.java)
        }
    }
}