package com.example.remotedatasource.serviceProvider

import com.example.repository.dto.remote.RemoteCountryDto

interface CountryServiceProvider {
    suspend fun getCountries(): List<RemoteCountryDto>
}