package com.rootdown.dev.codingchallengeoneadidev.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.repo.CharacterImpl
import com.rootdown.dev.codingchallengeoneadidev.viewmodel.feature_character_explorer.CharacterExplorerViewModel


/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val repository: CharacterImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterExplorerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharacterExplorerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}