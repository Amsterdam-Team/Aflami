package com.amsterdam.repository.mapper

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.dto.remote.CountryRemoteDto

fun CountryLocalDto.toEntity(): Country =
    Country(
        countryName = name,
        countryIsoCode = isoCode
    )

fun Country.toLocalDto(storedLanguage: String): CountryLocalDto =
    CountryLocalDto(
        name = countryName,
        storedLanguage = storedLanguage,
        isoCode = countryIsoCode
    )

fun CountryRemoteDto.toEntity(): Country =
    Country(
        countryName = nativeName,
        countryIsoCode = isoCode
    )

fun CountryRemoteDto.toLocalDto(storedLanguage: String): CountryLocalDto =
    CountryLocalDto(
        name = nativeName,
        storedLanguage = storedLanguage,
        isoCode = isoCode
    )

fun List<CountryRemoteDto>.toLocalDtoList(storedLanguage: String): List<CountryLocalDto> =
    map { it.toLocalDto(storedLanguage) }