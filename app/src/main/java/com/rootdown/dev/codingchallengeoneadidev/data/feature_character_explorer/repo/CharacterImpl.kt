package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.repo

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadAppDatabase
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.paging_data_sources.CharacterRemoteMediator
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net.ApiCharacterExplorer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterImpl @Inject constructor(
    private val api: ApiCharacterExplorer,
    private val db: BreakingBadAppDatabase
) : Character {
    val profile_ls: LiveData<List<BreakingBadChar>> = db.characterDao().getChars()
    @ExperimentalPagingApi
    fun getCharacters(query: String): Flow<PagingData<BreakingBadChar>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory =  { db.characterDao().charsByName(dbQuery) }
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = CharacterRemoteMediator(
                query,
                api,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }
}