package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.repository.datasource.local.LocalRecentSearchDataSource
import com.example.repository.dto.local.LocalSearchDto

class RecentSearchDataSourceImpl(
    private val dao: RecentSearchDao
) : LocalRecentSearchDataSource {

    override suspend fun insertOrReplaceSearch(search: LocalSearchDto) {
        dao.insertOrReplaceSearch(search)
    }

    override suspend fun getRecentSearches(): List<String> {
        return dao.getRecentSearches()
    }

    override suspend fun getSearchInfo(keyword: String): LocalSearchDto? {
        return dao.getSearchInfo(keyword)
    }

    override suspend fun deleteAllSearches() {
        dao.deleteAllSearches()
    }

    override suspend fun deleteSearchByKeyword(keyword: String) {
        dao.deleteSearchByKeyword(keyword)
    }
}