package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net

import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar


sealed class CharacterApiResult {
    data class Success(val data: List<BreakingBadChar>) : CharacterApiResult()
    data class Error(val e: Exception) : CharacterApiResult()
}