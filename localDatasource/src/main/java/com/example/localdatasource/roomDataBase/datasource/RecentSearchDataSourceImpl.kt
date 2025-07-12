package com.example.localdatasource.roomDataBase.datasource

import com.example.entity.Movie
import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.repository.datasource.local.LocalRecentSearchDataSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.relation.SearchWithMovies
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours

class RecentSearchDataSourceImpl(
    private val dao: RecentSearchDao
) : LocalRecentSearchDataSource {

    override suspend fun insertOrReplaceSearch(search: LocalSearchDto) {
        dao.insertOrReplaceSearch(search)
    }

    override suspend fun getRecentSearches(): List<String> {
        return dao.getRecentSearches()
    }

    override suspend fun getSearchByKeyword(keyword: String): LocalSearchDto? {
        return dao.getSearchInfo(keyword)
    }

    override suspend fun getSearchByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): SearchWithMovies {
        return dao.getSearchByKeyword(keyword, searchType)
    }

    override suspend fun insertOrReplaceAllMoviesWithSearchData(
        movies: List<Movie>,
        searchKeyword: String,
        searchType: SearchType,
        rating: Int?,
        category: String?
    ) {
        val expireDate = getExpireDate()
        movies.map { movie ->
            LocalSearchDto(
                searchKeyword = searchKeyword,
                searchType = searchType,
                expireDate = expireDate
            ) // todo wrong place for mapping
        }.forEach { insertOrReplaceSearch(it) }
    }

    override suspend fun deleteAllSearches() {
        dao.deleteAllSearches()
    }

    override suspend fun deleteSearchByKeyword(keyword: String) {
        dao.deleteSearchByKeyword(keyword)
    }

    override suspend fun deleteSearchByExpireDate(expireDate: Instant) {
        dao.deleteAllSearches()
    }

    private fun getExpireDate(): Instant {
        return Clock.System.now().plus(1.hours) // todo wrong place for mapping
    }
}