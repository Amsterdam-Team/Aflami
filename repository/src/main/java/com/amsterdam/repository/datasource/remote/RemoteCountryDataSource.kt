package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteCountryDto

interface RemoteCountryDataSource {
    suspend fun getAllCountries(): List<RemoteCountryDto>
}