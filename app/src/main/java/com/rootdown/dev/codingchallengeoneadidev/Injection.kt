package com.rootdown.dev.codingchallengeoneadidev

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadAppDatabase
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net.ApiCharacterExplorer
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.repo.CharacterImpl
import com.rootdown.dev.codingchallengeoneadidev.viewmodel.ViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    private fun provideBreakingBadAppRepository(context: Context): CharacterImpl {
        return CharacterImpl(ApiCharacterExplorer.ini(), BreakingBadAppDatabase.getInstance(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideBreakingBadAppRepository(context))
    }
}