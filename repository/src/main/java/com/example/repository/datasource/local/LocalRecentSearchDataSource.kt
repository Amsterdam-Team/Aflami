package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

interface LocalRecentSearchDataSource {
    suspend fun upsertResentSearch(search: LocalSearchDto)

    suspend fun getRecentSearches(): List<LocalSearchDto>

    suspend fun deleteAllSearches()

    suspend fun deleteSearchByKeyword(keyword: String)

    suspend fun deleteSearchByKeywordAndType(keyword: String, searchType: SearchType)

    suspend fun deleteExpiredSearches(currentDate: Instant)

    suspend fun getSearchByKeywordAndType(keyword: String, searchType: SearchType): LocalSearchDto?

}