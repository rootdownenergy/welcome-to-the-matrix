package com.rootdown.dev.codingchallengeoneadidev.di

import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.CharacterDao
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.RemoteKeysDao
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net.ApiCharacterExplorer
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.repo.Character
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.repo.CharacterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCharacterImpl(
        api: ApiCharacterExplorer,
        dao: CharacterDao,
        daoRemote: RemoteKeysDao
    ) = CharacterImpl(api,dao,daoRemote) as Character
}