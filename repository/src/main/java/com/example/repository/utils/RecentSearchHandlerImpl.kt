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
            onSuccess = { !isSearchExpired(it) },
            onFailure = { false }
        )
    }

    override suspend fun deleteRecentSearch(keyword: String, searchType: SearchType) {
        tryToExecute(
            function = {
                recentSearchLocalSource.deleteRecentSearchByKeywordAndType(keyword, searchType)
            },
            onSuccess = {},
            onFailure = {}
        )
    }

    private fun isSearchExpired(recentSearch: LocalSearchDto?): Boolean {
        if (recentSearch == null) return true
        return recentSearch.expireDate > Clock.System.now()
    }

}