package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteCountryDto

interface RemoteCountryDataSource {
    suspend fun getAllCountries(): List<RemoteCountryDto>
}