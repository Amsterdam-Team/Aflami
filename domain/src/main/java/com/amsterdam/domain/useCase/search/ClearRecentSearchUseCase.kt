package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.RecentSearchRepository

class ClearRecentSearchUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend operator fun invoke(searchKeyword: String) {
        recentSearchRepository.deleteRecentSearch(searchKeyword = searchKeyword)
    }
}