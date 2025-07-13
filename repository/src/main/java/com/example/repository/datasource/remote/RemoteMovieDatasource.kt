package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteMovieResponse

interface RemoteMovieDatasource {
    suspend fun getMoviesByKeyword(
        keyword: String,
        rating: Float = 0f,
        genreId: Int? = null
    ): RemoteMovieResponse

    suspend fun getMoviesByActorName(
        name: String
    ): RemoteMovieResponse

    suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String
    ): RemoteMovieResponse
}