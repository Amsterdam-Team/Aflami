package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType

interface LocalMovieDataSource {
    suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType,
        rating: Int?,
        category: String?
    ): List<MovieWithCategories>

    suspend fun addAllMoviesWithSearchData(
        movies: List<LocalMovieDto>,
        searchKeyword: String,
        searchType: SearchType,
        rating: Int?,
        category: String?
    )
}