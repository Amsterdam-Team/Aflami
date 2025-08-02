package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import kotlinx.datetime.Instant

interface PopularMovieLocalSource {
    suspend fun addPopularMovies(movies: List<LocalMovieDto>)

    suspend fun deleteExpiredPopularMovies(expirationTime: Instant, storedLanguage: String)
}