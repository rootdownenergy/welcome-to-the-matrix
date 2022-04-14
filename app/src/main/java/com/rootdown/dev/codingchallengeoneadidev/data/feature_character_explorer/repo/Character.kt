package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.repo

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import kotlinx.coroutines.flow.Flow

interface Character {
    fun profileLs(): LiveData<List<BreakingBadChar>>
    @ExperimentalPagingApi
    fun getCharacters(query: String): Flow<PagingData<BreakingBadChar>>
}