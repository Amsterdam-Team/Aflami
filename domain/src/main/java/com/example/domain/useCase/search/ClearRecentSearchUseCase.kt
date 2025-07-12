package com.example.domain.useCase.search

import com.example.domain.repository.RecentSearchRepository

class ClearRecentSearchUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend operator fun invoke(searchKeyword: String) {
        recentSearchRepository.deleteRecentSearch(searchKeyword = searchKeyword)
    }
}