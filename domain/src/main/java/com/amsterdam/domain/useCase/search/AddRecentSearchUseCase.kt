package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.RecentSearchRepository

class AddRecentSearchUseCase(
    private val recentSearchRepository: RecentSearchRepository,
) {
    suspend operator fun invoke(keyword: String) {
        if (keyword.isEmpty()) return
        recentSearchRepository.upsertRecentSearch(keyword)
    }
}