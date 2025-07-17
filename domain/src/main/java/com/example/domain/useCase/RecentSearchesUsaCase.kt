package com.example.domain.useCase

import com.example.domain.repository.RecentSearchRepository

class RecentSearchesUsaCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend fun addRecentSearch(keyword: String) {
        if (keyword.isBlank()) return
        recentSearchRepository.addRecentSearch(keyword)
    }

    suspend fun getRecentSearches(): List<String> {
        return recentSearchRepository.getRecentSearches()
    }

    suspend fun deleteRecentSearch(searchKeyword: String) {
        recentSearchRepository.deleteRecentSearch(searchKeyword = searchKeyword)
    }

    suspend fun deleteRecentSearches() {
        recentSearchRepository.deleteRecentSearches()
    }
}