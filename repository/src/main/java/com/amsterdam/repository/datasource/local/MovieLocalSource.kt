package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import kotlinx.datetime.Instant

interface MovieLocalSource {
    suspend fun getMovieById(movieId: Long, storedLanguage: String): LocalMovieDto?

    suspend fun incrementGenreInterest(categoryId: Long)

    suspend fun insertMovie(movie: LocalMovieDto)

    suspend fun addMovieWithCategories(
        movie: LocalMovieDto,
        categoryIds: List<Long>,
        storedLanguage: String
    )

    suspend fun getTopRatedMovies(storedLanguage: String): List<LocalMovieDto>

    suspend fun getPopularMovies(storedLanguage: String): List<MovieWithCategories>

    suspend fun getUpcomingMovies(storedLanguage: String): List<MovieWithCategories>

    suspend fun addPopularMovies(movies: List<LocalMovieDto>)

    suspend fun deleteExpiredPopularMovies(expirationTime: Instant, storedLanguage: String)

    suspend fun addTopRatedMovies(movies: List<LocalMovieDto>)

    suspend fun deleteAllExpiredTopRatedMovies(expirationTime: Instant, storedLanguage: String)

    suspend fun addUpcomingMovies(movies: List<LocalMovieDto>)

    suspend fun deleteExpiredUpcomingMovies(expirationTime: Instant, storedLanguage: String)

}