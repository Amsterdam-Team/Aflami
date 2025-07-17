package com.example.repository.mapper.local

import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Country.toLocal(): LocalCountryDto = LocalCountryDto(
    name = countryName,
    isoCode = countryIsoCode
)

fun LocalCountryDto.toDomain(): Country = Country(
    countryName = name,
    countryIsoCode = isoCode
)

fun Flow<List<LocalCountryDto>>.toCountryListFlow(): Flow<List<Country>> {
    return this.map { list -> list.map { it.toDomain() } }
}

fun List<LocalCountryDto>.toCountries(): List<Country> = map { it.toDomain() }
