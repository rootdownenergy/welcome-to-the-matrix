package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db


import com.google.gson.annotations.SerializedName

data class CharacterSearchResult (
    val data: List<BreakingBadChar>?,
    val nextPage: Int?
)