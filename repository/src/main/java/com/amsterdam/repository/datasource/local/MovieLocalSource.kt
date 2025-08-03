package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

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

    suspend fun addPopularMovies(movies: List<LocalMovieDto>)

    suspend fun deleteExpiredPopularMovies(expirationTime: Instant, storedLanguage: String)

    suspend fun addTopRatedMovies(movies: List<LocalMovieDto>)

    suspend fun deleteAllExpiredTopRatedMovies(expirationTime: Instant, storedLanguage: String)

    suspend fun addUpcomingMovies(movies: List<LocalMovieDto>)

    suspend fun deleteExpiredUpcomingMovies(expirationTime: Instant, storedLanguage: String)
}