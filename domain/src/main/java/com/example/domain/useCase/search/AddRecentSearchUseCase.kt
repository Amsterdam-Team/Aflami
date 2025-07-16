package com.example.domain.useCase.search

import com.example.domain.repository.RecentSearchRepository

class AddRecentSearchUseCase(
    private val recentSearchRepository: RecentSearchRepository,
) {
    suspend operator fun invoke(keyword: String) {
        if (keyword.isBlank()) return
        recentSearchRepository.upsertRecentSearch(keyword)
    }
}