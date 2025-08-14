package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.repository.datasource.local.RecentSearchLocalDataSource
import com.amsterdam.repository.dto.local.SearchLocalDto
import com.amsterdam.repository.mapper.toEntityList
import javax.inject.Inject

class RecentSearchRepositoryImpl @Inject constructor(
    private val recentSearchLocalDataSource: RecentSearchLocalDataSource
) : RecentSearchRepository {

    override suspend fun getAllRecentSearches(): List<String> {
        return recentSearchLocalDataSource.getRecentSearches().toEntityList()
    }

    override suspend fun deleteAllRecentSearches() {
        recentSearchLocalDataSource.deleteRecentSearches()
    }

    override suspend fun deleteRecentSearch(searchKeyword: String) {
        recentSearchLocalDataSource.deleteRecentSearchByKeyword(searchKeyword)
    }

    override suspend fun addRecentSearch(searchKeyword: String) {
        recentSearchLocalDataSource.upsertRecentSearch(SearchLocalDto(searchKeyword))
    }

}