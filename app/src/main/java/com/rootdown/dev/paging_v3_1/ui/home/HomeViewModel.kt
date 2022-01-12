package com.rootdown.dev.paging_v3_1.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.rootdown.dev.paging_v3_1.data.DatabaseStrain
import com.rootdown.dev.paging_v3_1.data.Repo
import com.rootdown.dev.paging_v3_1.db.RepoDatabase
import com.rootdown.dev.paging_v3_1.repo.ProfileRepository
import com.rootdown.dev.paging_v3_1.repo.StrainsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor (application: Application, state: SavedStateHandle) : AndroidViewModel(application) {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val strainsRepository = StrainsRepository(RepoDatabase.getInstance(application))
    private val profileRepository = ProfileRepository(RepoDatabase.getInstance(application))

    val strain = strainsRepository.strain
    val strainId = state.get<String>("strainId")
    val strainDetailed: LiveData<DatabaseStrain> = strainsRepository.getStrain(strainId)

    val profile = profileRepository
    val getProfileId = state.get<String?>("profile")
    val profileDetailed: LiveData<Repo> = profileRepository.getProfile(getProfileId)

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                strainsRepository.refreshStrains()

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
            }
        }
    }

}