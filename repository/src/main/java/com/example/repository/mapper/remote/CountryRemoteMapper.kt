package com.example.repository.mapper.remote

import com.example.domain.mapper.DomainMapper
import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto
import com.example.repository.dto.remote.RemoteCountryDto

class CountryRemoteMapper: DomainMapper<Country, RemoteCountryDto> {

    fun toLocalCountries(remoteCountries: List<RemoteCountryDto>): List<LocalCountryDto> {
        return remoteCountries.map { toLocalCountry(it) }
    }


    private fun toLocalCountry(remoteCountryDto: RemoteCountryDto): LocalCountryDto {
        return LocalCountryDto(
            name = remoteCountryDto.nativeName,
            isoCode = remoteCountryDto.isoCode
        )
    }

    override fun toDomain(dto: RemoteCountryDto): Country {
        return Country(
            countryName = dto.nativeName,
            countryIsoCode = dto.isoCode,
        )
    }
}