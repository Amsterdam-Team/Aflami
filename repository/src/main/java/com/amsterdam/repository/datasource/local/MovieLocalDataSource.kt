package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import kotlinx.datetime.Instant

interface MovieLocalDataSource {
    suspend fun getMovieById(movieId: Long, storedLanguage: String): MovieLocalDto?

    suspend fun incrementGenreInterest(categoryId: Long)

    suspend fun upsertMovie(movie: MovieLocalDto)

    suspend fun upsertMovieWithCategories(
        movie: MovieLocalDto,
        categoryIds: List<Long>,
        storedLanguage: String
    )

    suspend fun getTopRatedMovies(storedLanguage: String): List<MovieLocalDto>

    suspend fun getPopularMovies(storedLanguage: String): List<MovieWithCategories>

    suspend fun getUpcomingMovies(storedLanguage: String): List<MovieWithCategories>

    suspend fun upsertPopularMovies(movies: List<MovieLocalDto>)

    suspend fun deleteExpiredPopularMovies(expirationTime: Instant, storedLanguage: String)

    suspend fun upsertTopRatedMovies(movies: List<MovieLocalDto>)

    suspend fun deleteAllExpiredTopRatedMovies(expirationTime: Instant, storedLanguage: String)

    suspend fun upsertUpcomingMovies(movies: List<MovieLocalDto>)

    suspend fun deleteExpiredUpcomingMovies(expirationTime: Instant, storedLanguage: String)

}