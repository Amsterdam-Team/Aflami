package com.example.repository.utils

import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock

class RecentSearchHandlerImpl(
    private val recentSearchLocalSource: RecentSearchLocalSource
) : RecentSearchHandler {
    override suspend fun isExpired(keyword: String, searchType: SearchType): Boolean {
        return tryToExecute(
            function = { recentSearchLocalSource.getSearchByKeywordAndType(keyword, searchType) },
            onSuccess = { isSearchExpired(it) },
            onFailure = { true }
        )
    }

    override suspend fun deleteExpiredRecentSearch(keyword: String, searchType: SearchType) {
        tryToExecute(
            function = {
                recentSearchLocalSource.deleteExpiredRecentSearches(Clock.System.now())
                recentSearchLocalSource.deleteRecentSearchRelationWithMovie(keyword, searchType)
            },
            onSuccess = {},
            onFailure = {}
        )
    }

    private fun isSearchExpired(recentSearch: LocalSearchDto?): Boolean {
        if (recentSearch == null) return true
        return recentSearch.expireDate < Clock.System.now()
    }

}