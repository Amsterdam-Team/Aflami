package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.CountryRemoteDto

fun createRemoteCountryDto(
    englishName: String = "Egypt",
    isoCode: String = "EG",
    nativeName: String = "مصر"
): CountryRemoteDto {
    return CountryRemoteDto(
        englishName = englishName,
        isoCode = isoCode,
        nativeName = nativeName
    )
}