package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.SearchLocalDto

interface RecentSearchLocalDataSource {
    suspend fun upsertRecentSearch(recentSearch: SearchLocalDto)

    suspend fun getRecentSearches(): List<SearchLocalDto>


    suspend fun deleteRecentSearches()

    suspend fun deleteRecentSearchByKeyword(
        keyword: String)
}