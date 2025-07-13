package com.example.domain.repository

import com.example.entity.Movie

interface MovieRepository {
    suspend fun getMoviesByKeyword(keyword: String, rating: Float = 0f): List<Movie>
    suspend fun getMoviesByActor(actorName: String): List<Movie>
    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie>
}
