package com.rootdown.dev.codingchallengeoneadidev.di

import android.content.Context
import androidx.room.Room
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.AppDatabase
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.CharacterDao
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideBrakingBadAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appdb"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(appDatabase: AppDatabase): CharacterDao {
        return appDatabase.characterDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(appDatabase: AppDatabase): RemoteKeysDao {
        return appDatabase.remoteKeysDao()
    }
}