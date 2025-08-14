package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.CountryRemoteDto

interface CountryRemoteDataSource {
    suspend fun getCountries(): List<CountryRemoteDto>
}