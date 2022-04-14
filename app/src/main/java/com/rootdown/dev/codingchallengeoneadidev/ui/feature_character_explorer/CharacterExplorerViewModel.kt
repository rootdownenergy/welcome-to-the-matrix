package com.rootdown.dev.codingchallengeoneadidev.ui.feature_character_explorer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.repo.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharacterExplorerViewModel @Inject constructor(
    private val  repo: Character
    ) : ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<UiModel.CharItem>>? = null
    val x_profiles: LiveData<List<BreakingBadChar>> = repo.profileLs()

    @ExperimentalPagingApi
    fun searchRepo(queryString: String): Flow<PagingData<UiModel.CharItem>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<UiModel.CharItem>> = repo.getCharacters(queryString)
            .map { pagingData -> pagingData.map { UiModel.CharItem(it) } }
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

}
sealed class UiModel {
    data class CharItem(val repo: BreakingBadChar) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}
