package com.rootdown.dev.paging_v3_1.repo


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.rootdown.dev.paging_v3_1.api.UserProfile
import com.rootdown.dev.paging_v3_1.api.UserStrain
import com.rootdown.dev.paging_v3_1.data.*
import com.rootdown.dev.paging_v3_1.db.RepoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserContent constructor(
    private val database: RepoDatabase,
) {

    val userRepos: LiveData<List<UserProfile>> = Transformations.map(database.userProfilesDao().getUserRepos()){
        it.asDomainModel()
    }
    val userStrains: LiveData<List<UserStrain>> = Transformations.map(database.userStrainsDao().getUserStrains()){
        it.asDomainModel()
    }
    //val userStrains: LiveData<List<UserStrain>> = database.userStrainsDao().getUserStrains()
    val updatedStrains: LiveData<List<DatabaseStrain>> = database.userStrainsDao().getStrainWithOwnerId(1)
    val updatedProfiles: LiveData<List<Repo>> = database.userProfilesDao().getProfileWithOwnerId(1L)

    suspend fun deleteUserProfile(id: Long)
    {
        withContext(Dispatchers.IO) {
            database.userProfilesDao().deleteUserProfile(id)
        }
    }
    suspend fun deleteUserStrain(id: Int)
    {
        withContext(Dispatchers.IO) {
            database.userStrainsDao().deleteUserStrain(id)
        }
    }
    suspend fun updateRepo(repo: Repo)
    {
        database.userProfilesDao().updateProfileOwnerId(repo)
    }
    suspend fun updateStrain(strain: DatabaseStrain)
    {
        database.userStrainsDao().updateStrainOwnerId(strain)
    }
    fun insertUserStrain(userStrain: UserDatabaseStrain) {
        database.userStrainsDao().insertUserDatabaseStrain(userStrain)
    }
    fun insertUserRepo(userRepo: UserRepo) {
        database.userProfilesDao().insertUserRepo(userRepo)
    }

    fun getStrain(strainId: String?): LiveData<UserDatabaseStrain> {
        return database.userStrainsDao().strainById(strainId)
    }

    fun getProfile(profileId: Long?): LiveData<UserRepo> {
        if (profileId == null)
        {
            val i = 1L
            return database.userProfilesDao().profileById(i)
        }
        else
        {
            return database.userProfilesDao().profileById(profileId)
        }
    }
}