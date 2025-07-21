package com.example.repository.utils

import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock

class RecentSearchHandlerImpl(
    private val recentSearchLocalSource: RecentSearchLocalSource
) : RecentSearchHandler {
    override suspend fun isRecentSearchExpired(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String
    ): Boolean {
        return try {
            isSearchExpired(
                recentSearchLocalSource.getSearchByKeywordAndType(
                    keyword,
                    searchType,
                    storedLanguage
                )
            )
        } catch (_: Exception) {
            true
        }
    }

    override suspend fun deleteRecentSearch(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String
    ) {
        if (isSearchExpired(
                recentSearchLocalSource.getSearchByKeywordAndType(
                    keyword,
                    searchType,
                    storedLanguage
                )
            )
        ) {
            recentSearchLocalSource.deleteRecentSearchByKeywordAndType(
                keyword,
                searchType,
                getDeviceLanguage()
            )
        }
    }

    private fun isSearchExpired(recentSearch: LocalSearchDto?): Boolean {
        return if (recentSearch == null) true else recentSearch.expireDate < Clock.System.now()
    }
}