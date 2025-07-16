package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteMovieResponse

interface MovieRemoteSource {
    suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int,
    ): RemoteMovieResponse

    suspend fun getMoviesByActorName(name: String): RemoteMovieResponse
    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): RemoteMovieResponse
}