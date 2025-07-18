package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto

class CountryRemoteSourceImpl(
    private val ktorClient: KtorClient,
) : CountryRemoteSource {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return ktorClient.tryToExecute { ktorClient.get(GET_COUNTRIES) }
    }

    private companion object {
        const val GET_COUNTRIES = "configuration/countries"
    }
}