package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.RecentSearchLocalSource
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import com.amsterdam.repository.mapper.local.RecentSearchLocalMapper
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class RecentSearchRepositoryImpl @Inject constructor(
    private val recentSearchLocalSource: RecentSearchLocalSource,
    private val preferences: AppPreferences,
    private val recentSearchMapper: RecentSearchLocalMapper,
) : RecentSearchRepository {
    override suspend fun addRecentSearch(searchKeyword: String) {
        addRecentSearch(searchKeyword, searchType = SearchType.BY_KEYWORD)
    }

    override suspend fun addRecentSearchForCountry(searchKeyword: String) {
        addRecentSearch(searchKeyword, searchType = SearchType.BY_COUNTRY)
    }

    override suspend fun addRecentSearchForActor(searchKeyword: String) {
        addRecentSearch(searchKeyword, searchType = SearchType.BY_ACTOR)
    }

    override suspend fun getAllRecentSearches(): List<String> {
        return recentSearchMapper.toEntityList(
            recentSearchLocalSource.getRecentSearches(SearchType.BY_KEYWORD)
        )
    }

    override suspend fun deleteAllRecentSearches() {
        recentSearchLocalSource.deleteRecentSearches()
    }

    override suspend fun deleteRecentSearch(searchKeyword: String) {
        recentSearchLocalSource.deleteRecentSearchByKeywordAndType(
            searchKeyword,
            SearchType.BY_KEYWORD
        )
    }

    private suspend fun addRecentSearch(searchKeyword: String, searchType: SearchType) {
        recentSearchLocalSource.upsertRecentSearch(
            LocalSearchDto(
                searchKeyword,
                searchType,
                preferences.getDeviceLanguage().first(),
                Clock.System.now().plus(1.hours)
            )
        )
    }
}