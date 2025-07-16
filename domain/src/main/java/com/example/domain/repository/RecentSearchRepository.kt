package com.example.domain.repository

interface RecentSearchRepository {

    suspend fun upsertRecentSearch(searchKeyword: String)
    suspend fun upsertRecentSearchForCountry(searchKeyword: String)
    suspend fun upsertRecentSearchForActor(searchKeyword: String)
    suspend fun getAllRecentSearches(): List<String>
    suspend fun getRecentSearchesByKeyword(): List<String>
    suspend fun deleteAllRecentSearches()
    suspend fun deleteRecentSearch(searchKeyword: String)

}