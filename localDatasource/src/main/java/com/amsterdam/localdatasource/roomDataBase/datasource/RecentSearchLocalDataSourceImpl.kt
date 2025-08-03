package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.repository.datasource.local.RecentSearchLocalSource
import com.amsterdam.repository.dto.local.LocalSearchDto
import javax.inject.Inject

class RecentSearchLocalDataSourceImpl @Inject constructor(
    private val dao: RecentSearchDao
) : RecentSearchLocalSource {

    override suspend fun upsertRecentSearch(recentSearch: LocalSearchDto) {
        dao.upsertRecentSearch(recentSearch)
    }

    override suspend fun getRecentSearches(): List<LocalSearchDto> {
        return dao.getRecentSearches()
    }

    override suspend fun deleteRecentSearches() {
        dao.deleteAllSearches()
    }

    override suspend fun deleteRecentSearchByKeywordAndType(
        keyword: String,
    ) {
        dao.deleteSearchByKeyword(keyword)
    }
}