package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

interface RecentSearchLocalSource {
    suspend fun upsertRecentSearch(recentSearch: LocalSearchDto)

    suspend fun getRecentSearches(searchType: SearchType): List<LocalSearchDto>

    suspend fun getSearchByKeywordAndType(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String
    ): LocalSearchDto?

    suspend fun deleteRecentSearches()

    suspend fun deleteRecentSearchByKeywordAndType(
        keyword: String,
        searchType: SearchType
    )

    suspend fun deleteExpiredRecentSearches(date: Instant)
}