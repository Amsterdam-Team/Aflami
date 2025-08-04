package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.RecentSearchRepository

class RecentSearchesUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend fun addRecentSearch(keyword: String) {
        keyword.takeIf { it.isNotBlank() }
            ?.let { recentSearchRepository.addRecentSearch(it) }
    }

    suspend fun getRecentSearches(): List<String> {
        return recentSearchRepository.getAllRecentSearches()
    }

    suspend fun deleteRecentSearch(searchKeyword: String) {
        recentSearchRepository.deleteRecentSearch(searchKeyword = searchKeyword)
    }

    suspend fun deleteRecentSearches() {
        recentSearchRepository.deleteAllRecentSearches()
    }
}