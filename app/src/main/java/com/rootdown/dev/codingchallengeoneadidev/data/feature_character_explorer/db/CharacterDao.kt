package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCharacter(chars: List<BreakingBadChar>)
    @Query("SELECT * FROM characters WHERE name LIKE :query")
    fun charsByName(query: String): PagingSource<Int, BreakingBadChar>
    @Query("DELETE FROM characters")
    suspend fun clearChars()
    @Query("SELECT * FROM characters")
    fun getChars(): LiveData<List<BreakingBadChar>>
    @Query("SELECT * FROM characters WHERE charId LIKe :query")
    fun getCharById(query: String?): LiveData<BreakingBadChar>
}