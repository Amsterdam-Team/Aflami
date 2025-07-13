package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.repository.datasource.local.LocalRecentSearchDataSource
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import com.amsterdam.repository.mapper.local.RecentSearchMapper
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

class RecentSearchRepositoryImpl(
    private val localRecentSearchDataSource: LocalRecentSearchDataSource,
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
        return recentSearchMapper.toDomainList(localRecentSearchDataSource.getRecentSearches())
    }

    override suspend fun deleteAllRecentSearches() {
        localRecentSearchDataSource.deleteAllSearches()
    }

    override suspend fun deleteRecentSearch(searchKeyword: String) {
        localRecentSearchDataSource.deleteSearchByKeyword(searchKeyword)
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
        localRecentSearchDataSource.upsertResentSearch(localSearchDto)
    }
}