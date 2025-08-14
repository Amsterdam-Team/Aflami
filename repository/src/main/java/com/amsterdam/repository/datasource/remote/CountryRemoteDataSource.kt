package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteCountryDto

interface CountryRemoteDataSource {
    suspend fun getCountries(): List<RemoteCountryDto>
}