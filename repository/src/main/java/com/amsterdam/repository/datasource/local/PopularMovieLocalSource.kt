package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto

interface PopularMovieLocalSource {
    suspend fun addPopularMovies(movies: List<LocalMovieDto>)

    suspend fun deleteExpiredPopularMovies(expirationTime: Long, storedLanguage: String)
}