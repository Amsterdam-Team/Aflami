package com.example.repository.mapper.remote

import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto
import com.example.repository.dto.remote.RemoteCountryDto

fun List<RemoteCountryDto>.toCountries(): List<Country> {
    return map { it.toCountry() }
}

fun List<RemoteCountryDto>.toLocalCountries(): List<LocalCountryDto> {
    return map { it.toLocalCountry() }
}

fun RemoteCountryDto.toCountry(): Country {
    return Country(
        countryName = nativeName,
        countryIsoCode = isoCode,
    )
}

fun RemoteCountryDto.toLocalCountry(): LocalCountryDto {
    return LocalCountryDto(
        name = nativeName,
        isoCode = isoCode
    )
}
