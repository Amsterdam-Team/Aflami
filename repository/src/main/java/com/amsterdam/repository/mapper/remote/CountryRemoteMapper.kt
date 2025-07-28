package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class CountryRemoteMapper @Inject constructor(): EntityMapper<RemoteCountryDto, Country> {
    override fun toEntity(dto: RemoteCountryDto): Country {
        return Country(
            countryName = dto.nativeName,
            countryIsoCode = dto.isoCode,
        )
    }
}