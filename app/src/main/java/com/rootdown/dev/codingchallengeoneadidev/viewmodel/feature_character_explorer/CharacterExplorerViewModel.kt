package com.rootdown.dev.codingchallengeoneadidev.viewmodel.feature_character_explorer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.repo.CharacterImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterExplorerViewModel(private val  repo: CharacterImpl) : ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<UiModel>>? = null
    val x_profiles: LiveData<List<BreakingBadChar>> = repo.profile_ls

    @ExperimentalPagingApi
    fun searchRepo(queryString: String): Flow<PagingData<UiModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<UiModel>> = repo.getCharacters(queryString)
            .map { pagingData -> pagingData.map { UiModel.CharItem(it) } }
            .map {
                it.insertSeparators<UiModel.CharItem, UiModel> { before, after ->
                    if (before == null) {
                        return@insertSeparators null
                    }
                    if (after == null) {
                        return@insertSeparators null
                    }
                    if(before.roundedCount > after.roundedCount)
                    {
                        if(after.roundedCount >= 1){
                            return@insertSeparators null
                        }
                        else
                        {
                            return@insertSeparators null
                        }
                    }
                    else {
                        // no separator
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

}
sealed class UiModel {
    data class CharItem(val repo: BreakingBadChar) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}

private val UiModel.CharItem.roundedCount: Int
    get() = 1