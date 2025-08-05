package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.remote.RemoteCountryDto

fun RemoteCountryDto.toEntity(): Country =
    Country(
        countryName = nativeName,
        countryIsoCode = isoCode
    )


fun List<RemoteCountryDto>.toEntityList(): List<Country> = map { it.toEntity() }