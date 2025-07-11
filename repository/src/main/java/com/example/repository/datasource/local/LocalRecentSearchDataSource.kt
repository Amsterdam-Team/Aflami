package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalSearchDto

interface LocalRecentSearchDataSource {
    suspend fun insertOrReplaceSearch(search: LocalSearchDto)

    suspend fun getRecentSearches(): List<String>

    suspend fun deleteAllSearches()

    suspend fun deleteSearchByKeyword(keyword: String)
}