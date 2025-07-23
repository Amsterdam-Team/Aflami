package com.example.repository.utils

import com.example.repository.dto.local.utils.SearchType

interface RecentSearchHandler {
    suspend fun isRecentSearchExpired(keyword: String, searchType: SearchType, storedLanguage: String): Boolean
    suspend fun deleteRecentSearch(keyword: String, searchType: SearchType, storedLanguage: String)
}