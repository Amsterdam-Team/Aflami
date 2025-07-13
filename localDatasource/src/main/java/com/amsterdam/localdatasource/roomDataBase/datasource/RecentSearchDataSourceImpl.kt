package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.repository.datasource.local.LocalRecentSearchDataSource
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

class RecentSearchDataSourceImpl(
    private val dao: RecentSearchDao
) : LocalRecentSearchDataSource {

    override suspend fun upsertResentSearch(search: LocalSearchDto) {
        dao.upsertRecentSearch(search)
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

    override suspend fun deleteAllSearches() {
        dao.deleteAllSearches()
    }

    override suspend fun deleteSearchByKeyword(keyword: String) {
        dao.deleteSearchByKeyword(keyword)
        dao.deleteSearchMovieCrossRefByKeyword(keyword)
    }

    override suspend fun deleteSearchByKeywordAndType(keyword: String, searchType: SearchType) {
        dao.deleteSearchByKeyword(keyword, searchType = searchType)
        dao.deleteSearchMovieCrossRefByKeyword(keyword, searchType = searchType)
    }

    override suspend fun deleteExpiredSearches(currentDate: Instant) {
        dao.deleteAllExpiredSearches(currentDate)
    }
}