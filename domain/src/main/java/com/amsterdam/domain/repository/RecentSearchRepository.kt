package com.amsterdam.domain.repository

interface RecentSearchRepository {
    suspend fun addRecentSearch(searchKeyword: String)
    suspend fun getAllRecentSearches(): List<String>
    suspend fun deleteAllRecentSearches()
    suspend fun deleteRecentSearch(searchKeyword: String)
}