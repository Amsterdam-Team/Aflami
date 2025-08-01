package com.amsterdam.repository.utils

import com.amsterdam.repository.datasource.local.RecentSearchLocalSource
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock
import javax.inject.Inject

class RecentSearchHandlerImpl @Inject constructor(
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
            recentSearchLocalSource.deleteRecentSearchByKeywordAndType(keyword, searchType)
        }
    }

    private fun isSearchExpired(recentSearch: LocalSearchDto?): Boolean {
        return if (recentSearch == null) true else recentSearch.expireDate < Clock.System.now()
    }
}