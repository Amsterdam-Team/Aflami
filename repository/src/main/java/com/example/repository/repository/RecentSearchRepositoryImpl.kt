package com.example.repository.repository

import com.example.domain.repository.RecentSearchRepository
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.RecentSearchMapper
import com.example.repository.utils.tryToExecute

class RecentSearchRepositoryImpl(
    private val recentSearchLocalSource: RecentSearchLocalSource,
    private val recentSearchMapper: RecentSearchMapper,
) : RecentSearchRepository {
    override suspend fun upsertRecentSearch(searchKeyword: String) =
        upsertRecentSearch(searchKeyword, searchType = SearchType.BY_KEYWORD)

    override suspend fun upsertRecentSearchForCountry(searchKeyword: String) =
        upsertRecentSearch(searchKeyword, searchType = SearchType.BY_COUNTRY)

    override suspend fun upsertRecentSearchForActor(searchKeyword: String) =
        upsertRecentSearch(searchKeyword, searchType = SearchType.BY_ACTOR)

    override suspend fun getAllRecentSearches(): List<String> {
        return tryToExecute(
            function = { recentSearchLocalSource.getRecentSearches() },
            onSuccess = { recentSearchMapper.toDomainList(it) },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    override suspend fun deleteAllRecentSearches() = tryToExecute(
            function = { recentSearchLocalSource.deleteRecentSearches() },
            onSuccess = { },
            onFailure = { aflamiException -> throw aflamiException }
        )

    override suspend fun deleteRecentSearch(searchKeyword: String) =
        tryToExecute(
            function = { recentSearchLocalSource.deleteRecentSearchByKeyword(searchKeyword) },
            onSuccess = { },
            onFailure = { aflamiException -> throw aflamiException }
        )

    private suspend fun upsertRecentSearch(
        searchKeyword: String,
        searchType: SearchType = SearchType.BY_KEYWORD
    ) = tryToExecute(
        function = {
            recentSearchLocalSource.upsertRecentSearch(
                recentSearchMapper.toLocalSearch(
                    searchKeyword, searchType
                )
            )
        },
            onSuccess = {},
            onFailure = { aflamiException -> throw aflamiException }
        )
}