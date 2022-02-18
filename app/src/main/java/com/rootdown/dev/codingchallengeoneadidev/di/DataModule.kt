package com.rootdown.dev.codingchallengeoneadidev.di

import android.content.Context
import androidx.room.Room
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BreakingBadAppDatabase::class.java, "bbdb.db")
}