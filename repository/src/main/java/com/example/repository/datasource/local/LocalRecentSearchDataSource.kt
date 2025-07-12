package com.example.repository.datasource.local

import com.example.entity.Movie
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

interface LocalRecentSearchDataSource {
    suspend fun insertOrReplaceSearch(search: LocalSearchDto)

    suspend fun getRecentSearches(): List<String>

    suspend fun deleteAllSearches()

    suspend fun deleteSearchByKeyword(keyword: String)

    suspend fun deleteSearchByExpireDate(expireDate: Instant)

    suspend fun getSearchByKeyword(keyword: String): LocalSearchDto?

    suspend fun getSearchByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): SearchWithMovies

    suspend fun insertOrReplaceAllMoviesWithSearchData(
        movies: List<Movie>,
        searchKeyword: String,
        searchType: SearchType,
        rating: Int?,
        category: String?
    )
}