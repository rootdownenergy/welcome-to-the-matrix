package com.rootdown.dev.paging_v3_1.model

import com.rootdown.dev.paging_v3_1.data.Repo

/**
 *
 */
sealed class RepoSearchResult {
    data class Success(val data: List<Repo>) : RepoSearchResult()
    data class Error(val error: Exception) : RepoSearchResult()
}