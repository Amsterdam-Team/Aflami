package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

class RecentSearchLocalSourceImpl(
    private val dao: RecentSearchDao
) : RecentSearchLocalSource {

    override suspend fun upsertRecentSearch(recentSearch: LocalSearchDto) {
        dao.upsertRecentSearch(recentSearch)
    }

    override suspend fun getRecentSearches(): List<LocalSearchDto> {
        return dao.getRecentSearches()
    }

    override suspend fun getSearchByKeywordAndType(
        keyword: String,
        searchType: SearchType
    ): LocalSearchDto? {
        return dao.getSearchByKeywordAndType(keyword)
    }

    override suspend fun deleteRecentSearches() {
        dao.deleteAllSearches()
    }

    override suspend fun deleteRecentSearchByKeyword(keyword: String) {
        dao.deleteSearchByKeyword(keyword)
        dao.deleteSearchMovieCrossRefByKeyword(keyword)
    }

    override suspend fun deleteRecentSearchByKeywordAndType(keyword: String, searchType: SearchType) {
        dao.deleteSearchByKeyword(keyword, searchType = searchType)
        dao.deleteSearchMovieCrossRefByKeyword(keyword, searchType = searchType)
    }

    override suspend fun deleteExpiredRecentSearches(date: Instant) {
        dao.deleteAllExpiredSearches(date)
    }
}