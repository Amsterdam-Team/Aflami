package com.example.domain.repository

import com.example.entity.Country
import com.example.entity.Movie

interface MovieRepository {
    suspend fun getMoviesByKeyword(keyword: String): List<Movie>
    suspend fun getMoviesByActor(actorName: String): List<Movie>
    suspend fun getMoviesByCountry(country: Country): List<Movie>
}
