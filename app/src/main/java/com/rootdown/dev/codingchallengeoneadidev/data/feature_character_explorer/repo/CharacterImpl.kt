package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.repo

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.CharacterDao
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.RemoteKeysDao
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.paging_data_sources.CharacterRemoteMediator
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net.ApiCharacterExplorer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterImpl @Inject constructor(
    private val api: ApiCharacterExplorer,
    private val dao: CharacterDao,
    private val daoRemote: RemoteKeysDao
) : Character {

    override fun profileLs(): LiveData<List<BreakingBadChar>> {
        return dao.getChars()
    }
    @ExperimentalPagingApi
    override fun getCharacters(query: String): Flow<PagingData<BreakingBadChar>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory =  { dao.charsByName(dbQuery) }
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = CharacterRemoteMediator(
                query,
                api,
                daoRemote,
                dao
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}