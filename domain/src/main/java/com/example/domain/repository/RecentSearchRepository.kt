package com.example.domain.repository

interface RecentSearchRepository {
    suspend fun insertOrReplaceRecentSearch(searchKeyword: String)
    suspend fun getAllRecentSearches(): List<String>
    suspend fun deleteAllRecentSearches()
    suspend fun deleteRecentSearch(searchKeyword: String)
}