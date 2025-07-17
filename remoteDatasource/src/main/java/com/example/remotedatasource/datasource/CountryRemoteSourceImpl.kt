package com.example.remotedatasource.datasource

import com.example.remotedatasource.base.BaseRemoteSource
import com.example.remotedatasource.client.KtorClient
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto

class CountryRemoteSourceImpl(
    ktorClient: KtorClient,
) : BaseRemoteSource(ktorClient), CountryRemoteSource {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return safeExecute { ktorClient.get(GET_COUNTRIES) }
    }

    private companion object {
        const val GET_COUNTRIES = "configuration/countries"
    }
}