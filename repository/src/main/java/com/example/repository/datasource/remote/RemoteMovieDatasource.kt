package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteMovieResponse

interface RemoteMovieDatasource {

    suspend fun getMoviesByKeyword(keyword: String): RemoteMovieResponse

    suspend fun discoverMovies(keyword: String, rating: Float, genreId: Int?): RemoteMovieResponse

    suspend fun getMoviesByActorName(
        name: String
    ): RemoteMovieResponse

    suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String
    ): RemoteMovieResponse
}