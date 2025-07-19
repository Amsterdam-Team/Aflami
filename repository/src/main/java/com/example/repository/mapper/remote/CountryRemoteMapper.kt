package com.example.repository.mapper.remote

import com.example.entity.Country
import com.example.repository.dto.remote.RemoteCountryDto
import com.example.repository.mapper.shared.EntityMapper

class CountryRemoteMapper : EntityMapper<RemoteCountryDto, Country> {
    override fun toEntity(dto: RemoteCountryDto): Country {
        return Country(
            countryName = dto.nativeName,
            countryIsoCode = dto.isoCode,
        )
    }
}