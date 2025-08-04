package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.remote.RemoteCountryDto

fun RemoteCountryDto.toCountryEntity(): Country =
    Country(
        countryName = nativeName,
        countryIsoCode = isoCode
    )


fun List<RemoteCountryDto>.toCountryEntityList(): List<Country> = map { it.toCountryEntity() }