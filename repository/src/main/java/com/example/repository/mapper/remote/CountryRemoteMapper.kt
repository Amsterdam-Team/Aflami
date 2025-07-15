package com.example.repository.mapper.remote

import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto
import com.example.repository.dto.remote.RemoteCountryDto

class CountryRemoteMapper {

    fun mapToCountries(remoteCountries: List<RemoteCountryDto>): List<Country> {
        return remoteCountries.map { mapToCountry(it) }
    }

    fun mapToLocalCountries(remoteCountries: List<RemoteCountryDto>): List<LocalCountryDto> {
        return remoteCountries.map { mapToLocalCountry(it) }
    }

    fun mapToCountry(remoteCountryDto: RemoteCountryDto): Country {
        return Country(
            countryName = remoteCountryDto.nativeName,
            countryIsoCode = remoteCountryDto.isoCode,
        )
    }

    private fun mapToLocalCountry(remoteCountryDto: RemoteCountryDto): LocalCountryDto {
        return LocalCountryDto(
            name = remoteCountryDto.nativeName,
            isoCode = remoteCountryDto.isoCode
        )
    }
}