package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.CountryRemoteDto
import retrofit2.http.GET

interface CountryApiService {

    @GET("configuration/countries")
    suspend fun getCountries(): List<CountryRemoteDto>
}