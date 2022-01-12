package com.rootdown.dev.paging_v3_1.api

import com.google.gson.annotations.SerializedName
import com.rootdown.dev.paging_v3_1.data.Repo

/**
 * Data class to hold repo responses from searchRepo API calls.
 */
data class RepoSearchResponse(
    @SerializedName("total") val total: Long,
    @SerializedName("data") val items: List<Repo> = emptyList(),
    val nextPage: Int?
)