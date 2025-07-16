package com.example.domain.useCase.search

import com.example.domain.repository.RecentSearchRepository

class GetRecentSearchesUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend fun getAllRecentSearches(): List<String> {
        return recentSearchRepository.getAllRecentSearches()
    }

    suspend fun getRecentSearchesByKeyWord(): List<String> {
        return recentSearchRepository.getRecentSearchesByKeyword()
    }
}