package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.LocalCountryDto

fun LocalCountryDto.toCountryEntity(): Country =
    Country(
        countryName = name,
        countryIsoCode = isoCode
    )

fun List<LocalCountryDto>.toCountryEntityList(): List<Country> = map { it.toCountryEntity() }
fun Country.LocalCountryDto(storedLanguage: String): LocalCountryDto =
    LocalCountryDto(
        name = countryName,
        storedLanguage = storedLanguage,
        isoCode = countryIsoCode
    )
