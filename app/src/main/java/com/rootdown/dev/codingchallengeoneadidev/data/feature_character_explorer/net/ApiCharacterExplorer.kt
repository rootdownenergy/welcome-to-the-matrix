package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net

import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCharacterExplorer {
    //call the get method for characters
    @GET("characters")
    suspend fun getCharacters(
        @Query("name") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ) : List<BreakingBadChar>
}