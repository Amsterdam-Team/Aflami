package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

interface RecentSearchLocalSource {
    suspend fun upsertRecentSearch(recentSearch: LocalSearchDto)
    suspend fun getRecentSearches(): List<LocalSearchDto>
    suspend fun getSearchByKeywordAndType(keyword: String, searchType: SearchType): LocalSearchDto?

    suspend fun deleteRecentSearches()
    suspend fun deleteRecentSearchByKeyword(keyword: String)
    suspend fun deleteRecentSearchByKeywordAndType(keyword: String, searchType: SearchType)
    suspend fun deleteExpiredRecentSearches(date: Instant)
}