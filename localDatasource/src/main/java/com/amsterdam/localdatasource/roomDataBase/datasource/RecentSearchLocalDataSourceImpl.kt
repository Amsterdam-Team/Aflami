package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.repository.datasource.local.RecentSearchLocalSource
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant
import javax.inject.Inject

class RecentSearchLocalDataSourceImpl @Inject constructor(
    private val dao: RecentSearchDao
) : RecentSearchLocalSource {

    override suspend fun upsertRecentSearch(recentSearch: LocalSearchDto) {
        dao.upsertRecentSearch(recentSearch)
    }

    override suspend fun getRecentSearches(searchType: SearchType): List<LocalSearchDto> {
        return dao.getRecentSearches(searchType)
    }

    override suspend fun getSearchByKeywordAndType(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String,
    ): LocalSearchDto? {
        return dao.getSearchByKeywordAndType(keyword, searchType, storedLanguage)
    }

    override suspend fun deleteRecentSearches() {
        dao.deleteAllSearches()
    }

    override suspend fun deleteRecentSearchByKeywordAndType(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String
    ) {
        dao.deleteSearchByKeyword(keyword, searchType, storedLanguage)
        dao.deleteSearchMovieCrossRefByKeyword(keyword, searchType, storedLanguage)
    }

    override suspend fun deleteExpiredRecentSearches(date: Instant) {
        dao.deleteAllExpiredSearches(date)
    }
}