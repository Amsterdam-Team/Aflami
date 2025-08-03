package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.local.utils.SearchType

interface MovieLocalSource {
    suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String,
        limit: Int,
        offset: Int
    ): List<MovieWithCategories>

    suspend fun addMoviesBySearchData(
        movies: List<LocalMovieDto>,
        searchKeyword: String,
        searchType: SearchType,
    )

    suspend fun addMovieWithCategories(
        movie: LocalMovieDto,
        categoryIds: List<Long>,
        storedLanguage: String
    )

    suspend fun getTopRatedMovies(storedLanguage: String): List<LocalMovieDto>

    suspend fun getMovieById(movieId: Long, storedLanguage: String): LocalMovieDto?

    suspend fun incrementGenreInterest(categoryId: Long)

    suspend fun insertMovie(movie : LocalMovieDto)

    suspend fun getPopularMovies(storedLanguage: String): List<MovieWithCategories>

    suspend fun getUpcomingMovies(storedLanguage: String): List<MovieWithCategories>
}