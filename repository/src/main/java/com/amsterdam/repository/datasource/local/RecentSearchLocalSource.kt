package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalSearchDto

interface RecentSearchLocalSource {
    suspend fun upsertRecentSearch(recentSearch: LocalSearchDto)

    suspend fun getRecentSearches(): List<LocalSearchDto>


    suspend fun deleteRecentSearches()

    suspend fun deleteRecentSearchByKeywordAndType(
        keyword: String)
}