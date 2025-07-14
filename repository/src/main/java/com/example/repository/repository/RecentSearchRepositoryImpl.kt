package com.example.repository.repository

import com.example.domain.repository.RecentSearchRepository
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.RecentSearchMapper
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

class RecentSearchRepositoryImpl(
    private val recentSearchLocalSource: RecentSearchLocalSource,
    private val recentSearchMapper: RecentSearchMapper,
) : RecentSearchRepository {
    override suspend fun upsertRecentSearch(searchKeyword: String) {
        upsertRecentSearch(searchKeyword, searchType = SearchType.BY_KEYWORD)
    }

    override suspend fun upsertRecentSearchForCountry(searchKeyword: String) {
        upsertRecentSearch(searchKeyword, searchType = SearchType.BY_COUNTRY)
    }

    override suspend fun upsertRecentSearchForActor(searchKeyword: String) {
        upsertRecentSearch(searchKeyword, searchType = SearchType.BY_ACTOR)
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

    private suspend fun upsertRecentSearch(
        searchKeyword: String,
        searchType: SearchType = SearchType.BY_KEYWORD
    ) {
        val localSearchDto = LocalSearchDto(
            searchKeyword = searchKeyword,
            searchType = searchType,
            expireDate = Clock.System.now().plus(1.hours)
        )
        recentSearchLocalSource.upsertRecentSearch(localSearchDto)
    }
}