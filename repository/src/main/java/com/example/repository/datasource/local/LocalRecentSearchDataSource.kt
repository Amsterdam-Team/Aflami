package com.example.repository.datasource.local

import com.example.entity.Movie
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.relation.SearchWithMovies
import com.example.repository.dto.local.utils.SearchType

interface LocalRecentSearchDataSource {
    suspend fun insertOrReplaceSearch(search: LocalSearchDto)

    suspend fun getRecentSearches(): List<String>

    suspend fun deleteAllSearches()

    suspend fun deleteSearchByKeyword(keyword: String)

    suspend fun getSearchByKeyword(keyword: String): LocalSearchDto?

    suspend fun getSearchByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<SearchWithMovies>

    suspend fun insertOrReplaceAllMoviesWithSearchData(
        movies: List<Movie>,
        searchKeyword: String,
        searchType: SearchType,
        rating: Int?,
        category: String?
    )
}