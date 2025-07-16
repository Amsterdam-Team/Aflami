package com.example.repository.utils

import com.example.repository.dto.local.utils.SearchType

interface RecentSearchHandler {
    suspend fun isExpired(keyword: String, searchType: SearchType): Boolean
    suspend fun deleteRecentSearchRelationWithMovie(keyword: String, searchType: SearchType)
    suspend fun deleteExpiredRecentSearch()
}