package com.amsterdam.repository.mapper

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.dto.remote.CountryRemoteDto

fun CountryLocalDto.toEntity(): Country {
    return Country(
        countryName = name,
        countryIsoCode = isoCode
    )
}

fun Country.toLocalDto(storedLanguage: String): CountryLocalDto {
    return CountryLocalDto(
        name = countryName,
        storedLanguage = storedLanguage,
        isoCode = countryIsoCode
    )
}

fun CountryRemoteDto.toEntity(): Country {
    return Country(
        countryName = nativeName,
        countryIsoCode = isoCode
    )
}

fun CountryRemoteDto.toLocalDto(storedLanguage: String): CountryLocalDto {
    return CountryLocalDto(
        name = nativeName,
        storedLanguage = storedLanguage,
        isoCode = isoCode
    )
}

fun List<CountryRemoteDto>.toLocalDtoList(storedLanguage: String): List<CountryLocalDto> {
    return map { it.toLocalDto(storedLanguage) }
}