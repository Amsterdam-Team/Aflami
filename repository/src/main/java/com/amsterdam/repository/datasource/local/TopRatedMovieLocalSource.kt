package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import kotlinx.datetime.Instant

interface TopRatedMovieLocalSource {
    suspend fun addTopRatedMovies(movies: List<LocalMovieDto>)
    suspend fun deleteAllExpiredTopRatedMovies(expirationTime: Instant, storedLanguage: String)
}