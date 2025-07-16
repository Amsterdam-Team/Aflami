package com.example.domain.useCase.search

import com.example.domain.repository.RecentSearchRepository

class AddRecentSearchUseCase(
    private val recentSearchRepository: RecentSearchRepository,
) {
    suspend fun addRecentSearchForKeyword(keyword: String) {
        if (keyword.isBlank()) return
        recentSearchRepository.upsertRecentSearch(keyword)
    }

    suspend fun addRecentSearchForCountry(keyword: String) {
        if (keyword.isBlank()) return
        recentSearchRepository.upsertRecentSearchForCountry(keyword)
    }

    suspend fun addRecentSearchForActor(keyword: String) {
        if (keyword.isBlank()) return
        recentSearchRepository.upsertRecentSearchForActor(keyword)
    }
}