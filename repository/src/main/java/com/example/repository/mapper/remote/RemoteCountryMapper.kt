package com.example.repository.mapper.remote

import com.example.entity.Country
import com.example.repository.dto.remote.RemoteCountryDto

class RemoteCountryMapper {
    fun mapToDomain(dto: RemoteCountryDto): Country {
        return Country(
            countryName = dto.nativeName,
            countryIsoCode = dto.isoCode,
        )
    }
}