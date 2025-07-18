package com.example.repository.repository

import com.example.domain.repository.RecentSearchRepository
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.RecentSearchMapper

class RecentSearchRepositoryImpl(
    private val recentSearchLocalSource: RecentSearchLocalSource,
    private val recentSearchMapper: RecentSearchMapper,
) : RecentSearchRepository {
    override suspend fun addRecentSearch(searchKeyword: String) {
        addRecentSearch(searchKeyword, searchType = SearchType.BY_KEYWORD)
    }

    override suspend fun addRecentSearchForCountry(searchKeyword: String) {
        addRecentSearch(searchKeyword, searchType = SearchType.BY_COUNTRY)
    }

    override suspend fun addRecentSearchForActor(searchKeyword: String) {
        addRecentSearch(searchKeyword, searchType = SearchType.BY_ACTOR)
    }

    override suspend fun getAllRecentSearches(): List<String> {
        return recentSearchMapper.toDomainList(recentSearchLocalSource.getRecentSearches())
    }

    override suspend fun deleteAllRecentSearches() {
        recentSearchLocalSource.deleteRecentSearches()
    }

    override suspend fun deleteRecentSearch(searchKeyword: String) {
        recentSearchLocalSource.deleteRecentSearchByKeyword(searchKeyword)
    }

    private suspend fun addRecentSearch(searchKeyword: String, searchType: SearchType) {
        recentSearchLocalSource.upsertRecentSearch(
            recentSearchMapper.toLocalSearch(
                searchKeyword, searchType
            )
        )
    }
}