package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.paging_data_sources

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadAppDatabase
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.RemoteKeys
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net.ApiCharacterExplorer
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException


private const val CHAR_STARTING_PAGE_INDEX = 0
@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val query: String,
    private val api: ApiCharacterExplorer,
    private val db: BreakingBadAppDatabase
): RemoteMediator<Int, BreakingBadChar>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BreakingBadChar>
    ): RemoteMediator.MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
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
        try {
            /**
             * breaking bad api has the following
             * /api/characters?limit=10&offset=10
             *  build logic for if query not null else
             */
            val apiResponse = api.getCharacters(apiQuery, page, state.config.pageSize)
            val characters = apiResponse
            val endOfPaginationReached = characters.isEmpty()
            //val applicationScope = CoroutineScope
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.characterDao().clearChars()
                }
                val prevKey = if (page == CHAR_STARTING_PAGE_INDEX) null else page - 10
                val nextKey = if (endOfPaginationReached) null else page + 10
                val keys = characters.map {
                    RemoteKeys(repoId = it.charId.toLong(), prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys)
                db.characterDao().insertAllCharacter(characters)
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
                db.remoteKeysDao().remoteKeysRepoId(character.charId.toLong())
            }
    }
    private suspend fun getRemoteKeyFromFirstItem(state: PagingState<Int, BreakingBadChar>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                db.remoteKeysDao().remoteKeysRepoId(character.charId.toLong())
            }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, BreakingBadChar>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.charId?.let { repoId ->
                db.remoteKeysDao().remoteKeysRepoId(repoId.toLong())
            }
        }
    }
}