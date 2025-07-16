package com.example.domain.repository

import com.example.entity.Movie

interface MovieRepository {
    suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int,
    ): List<Movie>

    suspend fun getMoviesByActor(
        actorName: String,
        page: Int,
    ): List<Movie>

    suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String, page: Int): List<Movie>
}
