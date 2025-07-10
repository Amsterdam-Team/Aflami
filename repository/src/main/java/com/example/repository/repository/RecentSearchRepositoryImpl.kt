package com.example.repository.repository

import com.example.domain.repository.RecentSearchRepository
import com.example.repository.dataSource.local.LocalRecentSearchDataSource

class RecentSearchRepositoryImpl(private val localRecentSearchDataSource: LocalRecentSearchDataSource) :
    RecentSearchRepository {
    override suspend fun insertOrReplaceRecentSearch(searchKeyword: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getRecentSearch(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRecentSearches() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecentSearch(searchKeyword: String) {
        TODO("Not yet implemented")
    }
}