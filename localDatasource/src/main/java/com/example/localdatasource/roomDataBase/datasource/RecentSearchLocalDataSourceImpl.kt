package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

class RecentSearchLocalDataSourceImpl(
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

    override suspend fun deleteRecentSearchByKeyword(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String
    ) {
        dao.deleteSearchByKeyword(keyword, searchType, storedLanguage)
        dao.deleteSearchMovieCrossRefByKeyword(keyword, searchType, storedLanguage)
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