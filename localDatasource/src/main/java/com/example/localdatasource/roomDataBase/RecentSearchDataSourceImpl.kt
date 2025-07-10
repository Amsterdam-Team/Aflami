package com.example.localdatasource.roomDatabase

import com.example.localdatasource.roomDatabase.daos.RecentSearchDao
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

    override suspend fun deleteAllSearches() {
        dao.deleteAllSearches()
    }

    override suspend fun deleteSearchByKeyword(keyword: String) {
        dao.deleteSearchByKeyword(keyword)
    }
}