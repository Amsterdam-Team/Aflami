package com.example.repository.mapper.remote

import com.example.entity.Country
import com.example.repository.dto.remote.RemoteCountryDto

class CountryRemoteMapper {
    fun mapToCountry(remoteCountryDto: RemoteCountryDto): Country {
        return Country(
            countryName = remoteCountryDto.nativeName,
            countryIsoCode = remoteCountryDto.isoCode,
        )
    }
}