package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto

class CountryRemoteSourceImpl(
    private val networkClient: NetworkClient
) : CountryRemoteSource {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return safeCall { networkClient.get(GET_COUNTRIES) }
    }

    private companion object {
        const val GET_COUNTRIES = "configuration/countries"
    }
}