package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ RemoteKeys::class, BreakingBadChar::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListStringConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun  characterDao(): CharacterDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}