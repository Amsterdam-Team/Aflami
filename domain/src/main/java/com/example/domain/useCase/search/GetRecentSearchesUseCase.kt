package com.example.domain.useCase.search

import com.example.domain.repository.RecentSearchRepository

class GetRecentSearchesUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend operator fun invoke(): List<String> {
        return recentSearchRepository.getAllRecentSearches()
    }
}