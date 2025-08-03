package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import kotlinx.datetime.Instant

interface UpcomingMovieLocalSource {
    suspend fun addUpcomingMovies(movies: List<LocalMovieDto>)
    suspend fun deleteExpiredUpcomingMovies(expirationTime: Instant, storedLanguage: String)
}