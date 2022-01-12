package com.rootdown.dev.paging_v3_1.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rootdown.dev.paging_v3_1.repo.RootDownRepository
import com.rootdown.dev.paging_v3_1.ui.dashboard.SearchRepositoriesViewModel


/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val repository: RootDownRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchRepositoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}