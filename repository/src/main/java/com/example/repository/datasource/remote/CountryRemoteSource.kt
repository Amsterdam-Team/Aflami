package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteCountryDto

interface CountryRemoteSource {
    suspend fun getCountries(): List<RemoteCountryDto>
}