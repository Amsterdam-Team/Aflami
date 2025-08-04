package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.repository.datasource.local.RecentSearchLocalSource
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.mapper.local.toEntityList
import javax.inject.Inject

class RecentSearchRepositoryImpl @Inject constructor(
    private val recentSearchLocalSource: RecentSearchLocalSource
) : RecentSearchRepository {

    override suspend fun getAllRecentSearches(): List<String> {
        return recentSearchLocalSource.getRecentSearches().toEntityList()
    }

    override suspend fun deleteAllRecentSearches() {
        recentSearchLocalSource.deleteRecentSearches()
    }

    override suspend fun deleteRecentSearch(searchKeyword: String) {
        recentSearchLocalSource.deleteRecentSearchByKeywordAndType(searchKeyword)
    }

    override suspend fun addRecentSearch(searchKeyword: String) {
        recentSearchLocalSource.upsertRecentSearch(LocalSearchDto(searchKeyword))
    }

}