package com.example.domain.usecase.search

import com.example.domain.repository.RecentSearchRepository

class ClearAllRecentSearchesUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend operator fun invoke() {
        recentSearchRepository.deleteAllRecentSearches()
    }
}
