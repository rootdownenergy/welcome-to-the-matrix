package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ RemoteKeys::class, BreakingBadChar::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListStringConverter::class)
abstract class BreakingBadAppDatabase : RoomDatabase() {
    abstract fun  characterDao(): CharacterDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    companion object {

        @Volatile
        private var INSTANCE: BreakingBadAppDatabase? = null

        fun getInstance(context: Context): BreakingBadAppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                BreakingBadAppDatabase::class.java, "bbdb.db")
                .build()
    }
}