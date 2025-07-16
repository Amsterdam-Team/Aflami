package com.example.domain.repository

import com.example.entity.Movie

interface MovieRepository {
    suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int = 1,
    ): List<Movie>

    suspend fun getMoviesByActor(actorName: String): List<Movie>
    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie>
}
