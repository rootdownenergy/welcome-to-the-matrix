package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.paging_data_sources

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.*
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net.ApiCharacterExplorer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException



@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val query: String,
    private val api: ApiCharacterExplorer,
    private val dao: RemoteKeysDao,
    private val daoCharacterDao: CharacterDao
): RemoteMediator<Int, BreakingBadChar>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BreakingBadChar>
    ): RemoteMediator.MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                Log.w("KEY", remoteKeys.toString())
                remoteKeys?.nextKey?.minus(10) ?: CHAR_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyFromFirstItem(state)
                if (remoteKeys?.equals(false) == null) {
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                if (remoteKeys.prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyFromLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }
        val apiQuery = query
        var apiResponse = api.getCharacters(apiQuery, 25, page)
        try {
            /**
             * breaking bad api has the following
             * /api/characters?limit=10&offset=10
             *  build logic for if query not null else
             */
            if(apiQuery.isNotBlank()){
                apiResponse = api.getCharacters(apiQuery,1,0)
            } else {
                apiResponse = api.getCharacters(apiQuery, 25, page)
            }
            Log.w("TAG", apiQuery)
            Log.w("TAG", state.config.pageSize.toString())
            Log.w("TAG", page.toString())
            val endOfPaginationReached = apiResponse.isEmpty()
            //val applicationScope = CoroutineScope
            withContext(Dispatchers.IO) {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    dao.clearRemoteKeys()
                    daoCharacterDao.clearChars()
                }
                val prevKey = if (page == CHAR_STARTING_PAGE_INDEX) 0 else page - 10
                val nextKey = if (endOfPaginationReached) null else page + 10
                val keys = apiResponse.map {
                    RemoteKeys(repoId = it.charId.toLong(), prevKey = prevKey, nextKey = nextKey)
                }
                dao.insertAll(keys)
                daoCharacterDao.insertAllCharacter(apiResponse)

            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
    private suspend fun getRemoteKeyFromLastItem(state: PagingState<Int, BreakingBadChar>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                dao.remoteKeysRepoId(character.charId.toLong())
            }
    }
    private suspend fun getRemoteKeyFromFirstItem(state: PagingState<Int, BreakingBadChar>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                dao.remoteKeysRepoId(character.charId.toLong())
            }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, BreakingBadChar>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.charId?.let { repoId ->
                dao.remoteKeysRepoId(repoId.toLong())
            }
        }
    }

    companion object {
        private const val CHAR_STARTING_PAGE_INDEX = 0
    }
}