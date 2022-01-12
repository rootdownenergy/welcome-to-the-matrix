package com.rootdown.dev.paging_v3_1.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rootdown.dev.paging_v3_1.data.Repo

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Repo>)
    @Query("SELECT * FROM repos WHERE city LIKE :queryString "+
            "OR state LIKE :queryString "+
            "OR company_name LIKE :queryString "+
            "ORDER BY :queryString DESC")
    fun reposByName(queryString: String): PagingSource<Int, Repo>
    @Query("DELETE FROM repos")
    suspend fun clearRepos()
    @Query("SELECT * FROM repos")
    fun getprofiles(): LiveData<List<Repo>>

    @Query("SELECT * FROM repos WHERE company_name LIKE :queryString")
    fun getProfileById(queryString: String?): LiveData<Repo>

}