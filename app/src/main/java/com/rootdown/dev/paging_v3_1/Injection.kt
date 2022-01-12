package com.rootdown.dev.paging_v3_1


import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.rootdown.dev.paging_v3_1.api.RootDownService
import com.rootdown.dev.paging_v3_1.db.RepoDatabase
import com.rootdown.dev.paging_v3_1.repo.RootDownRepository
import com.rootdown.dev.paging_v3_1.ui.ViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    private fun provideRootDownRepository(context: Context): RootDownRepository {
        return RootDownRepository(RootDownService.create(), RepoDatabase.getInstance(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideRootDownRepository(context))
    }
}