package com.example.repository.repository

import com.example.domain.repository.RecentSearchRepository
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.toDomainList
import com.example.repository.utils.tryToExecute
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

class RecentSearchRepositoryImpl(
    private val recentSearchLocalSource: RecentSearchLocalSource
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
        return tryToExecute(
            function = { recentSearchLocalSource.getRecentSearches() },
            onSuccess = { it.toDomainList() },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    override suspend fun deleteAllRecentSearches() {
        tryToExecute(
            function = { recentSearchLocalSource.deleteRecentSearches() },
            onSuccess = { },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    override suspend fun deleteRecentSearch(searchKeyword: String) {
        tryToExecute(
            function = { recentSearchLocalSource.deleteRecentSearchByKeyword(searchKeyword) },
            onSuccess = { },
            onFailure = { aflamiException -> throw aflamiException }
        )
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
        tryToExecute(
            function = { recentSearchLocalSource.upsertRecentSearch(localSearchDto) },
            onSuccess = {},
            onFailure = { aflamiException -> throw aflamiException }
        )
    }
}