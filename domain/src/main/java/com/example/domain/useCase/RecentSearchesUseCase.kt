package com.example.domain.useCase

import com.example.domain.repository.RecentSearchRepository
import com.example.entity.Country

class RecentSearchesUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend fun addRecentSearch(keyword: String) {
        keyword.takeIf { it.isNotBlank() }
            ?.let { recentSearchRepository.addRecentSearch(it) }
    }

    suspend fun addRecentSearchForCountry(country: Country) {
        country.countryIsoCode.takeIf { it.isNotBlank() }
            ?.let { recentSearchRepository.addRecentSearchForCountry(it) }
    }

    suspend fun addRecentSearchForActor(actorName: String) {
        actorName.takeIf { it.isNotBlank() }
            ?.let { recentSearchRepository.addRecentSearchForActor(it) }
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