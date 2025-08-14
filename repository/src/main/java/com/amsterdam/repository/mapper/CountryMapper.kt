package com.amsterdam.repository.mapper

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.dto.remote.RemoteCountryDto

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

fun RemoteCountryDto.toEntity(): Country =
    Country(
        countryName = nativeName,
        countryIsoCode = isoCode
    )

fun RemoteCountryDto.toLocalDto(storedLanguage: String): CountryLocalDto =
    CountryLocalDto(
        name = nativeName,
        storedLanguage = storedLanguage,
        isoCode = isoCode
    )

fun List<RemoteCountryDto>.toLocalDtoList(storedLanguage: String): List<CountryLocalDto> =
    map { it.toLocalDto(storedLanguage) }