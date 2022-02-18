package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net.util

import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import retrofit2.http.GET

interface WebApiService {
    @GET(API_CHARACTERS)
    fun getBreakingBadChars(): List<BreakingBadChar>
}