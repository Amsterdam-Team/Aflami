package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.LocalCountryDto

fun LocalCountryDto.toEntity(): Country =
    Country(
        countryName = name,
        countryIsoCode = isoCode
    )

fun List<LocalCountryDto>.toEntityList(): List<Country> = map { it.toEntity() }
fun Country.LocalCountryDto(storedLanguage: String): LocalCountryDto =
    LocalCountryDto(
        name = countryName,
        storedLanguage = storedLanguage,
        isoCode = countryIsoCode
    )
