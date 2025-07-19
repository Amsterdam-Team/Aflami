package com.example.domain.repository

interface RecentSearchRepository {
    suspend fun addRecentSearch(searchKeyword: String)
    suspend fun addRecentSearchForCountry(searchKeyword: String)
    suspend fun addRecentSearchForActor(searchKeyword: String)
    suspend fun getAllRecentSearches(): List<String>
    suspend fun deleteAllRecentSearches()
    suspend fun deleteRecentSearch(searchKeyword: String)
}