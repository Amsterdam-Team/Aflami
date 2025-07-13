package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.remote.RemoteCountryDto

class RemoteCountryMapper {
    fun mapToDomain(dto: RemoteCountryDto): Country {
        return Country(
            countryName = dto.nativeName,
            countryIsoCode = dto.isoCode,
        )
    }
}