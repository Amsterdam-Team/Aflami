package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto

class CountryRemoteDataSourceImpl(
    private val networkClient: NetworkClient
) : CountryRemoteSource {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return responseCall { networkClient.get(GET_COUNTRIES) }
    }

    private companion object {
        const val GET_COUNTRIES = "configuration/countries"
    }
}