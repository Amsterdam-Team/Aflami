package com.example.repository.repository

import com.example.domain.repository.RecentSearchRepository
import com.example.repository.datasource.local.LocalRecentSearchDataSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock

class RecentSearchRepositoryImpl(
    private val localRecentSearchDataSource: LocalRecentSearchDataSource
) : RecentSearchRepository {
    override suspend fun insertOrReplaceRecentSearch(searchKeyword: String) {
        val localSearchDto = LocalSearchDto(
            searchKeyword = searchKeyword,
            searchType = SearchType.BY_KEYWORD,
            expireDate = Clock.System.now()
        )
        localRecentSearchDataSource.insertOrReplaceSearch(localSearchDto)
    }

    override suspend fun getAllRecentSearches(): List<String> {
        return localRecentSearchDataSource.getRecentSearches()
    }

    override suspend fun deleteAllRecentSearches() {
        localRecentSearchDataSource.deleteAllSearches()
    }

    override suspend fun deleteRecentSearch(searchKeyword: String) {
        localRecentSearchDataSource.deleteSearchByKeyword(searchKeyword)
    }
}