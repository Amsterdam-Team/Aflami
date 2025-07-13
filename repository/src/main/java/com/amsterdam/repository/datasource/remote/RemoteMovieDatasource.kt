package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteMovieResponse

interface RemoteMovieDatasource {
    suspend fun getMoviesByKeyword(
        keyword: String
    ): RemoteMovieResponse

    suspend fun getMoviesByActorName(
        name: String
    ): RemoteMovieResponse

    suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String
    ): RemoteMovieResponse
}