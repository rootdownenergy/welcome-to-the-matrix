package com.rootdown.dev.paging_v3_1.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rootdown.dev.paging_v3_1.data.*

@Dao
interface UserProfilesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserRepo(vararg userRepo: UserRepo)
    @Query("select * from userrepo")
    fun getUserRepos(): LiveData<List<UserRepo>>
    @Update
    suspend fun updateProfileOwnerId(repo: Repo)
    @Query("SELECT * FROM repos WHERE profileOwnerId =:q")
    fun getProfileWithOwnerId(q: Long): LiveData<List<Repo>>
    @Query("DELETE FROM userrepo WHERE id = :profileId")
    suspend fun deleteUserProfile(profileId: Long)
    @Query("SELECT * FROM userrepo WHERE id = :query")
    fun profileById(query: Long): LiveData<UserRepo>
}