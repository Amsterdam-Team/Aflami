package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalCountryDto
import com.amsterdam.repository.dto.remote.RemoteCountryDto

fun RemoteCountryDto.toLocalDto(storedLanguage: String): LocalCountryDto =
    LocalCountryDto(
        name = nativeName,
        storedLanguage = storedLanguage,
        isoCode = isoCode
    )

fun List<RemoteCountryDto>.toLocalDtoList(storedLanguage: String): List<LocalCountryDto> = map { it.toLocalDto(storedLanguage) }