package com.rootdown.dev.paging_v3_1.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rootdown.dev.paging_v3_1.data.*


@Database(
    entities = [Repo::class, RemoteKeys::class, DatabaseStrain::class, UserRepo::class, UserDatabaseStrain::class, DatabaseLatLng::class],
    version = 1,
    exportSchema = false
)
abstract class RepoDatabase : RoomDatabase() {

    abstract fun reposDao(): RepoDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun strainDao(): StrainDao
    abstract fun userStrainsDao(): UserStrainsDao
    abstract fun userProfilesDao(): UserProfilesDao
    abstract fun dbLatLng(): LatLngDao

    companion object {

        @Volatile
        private var INSTANCE: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                RepoDatabase::class.java, "RootDownGreen.db")
                .build()
    }
}