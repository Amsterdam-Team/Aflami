package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.RecentSearchRepository

class GetRecentSearchesUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend operator fun invoke(): List<String> {
        return recentSearchRepository.getAllRecentSearches()
    }
}