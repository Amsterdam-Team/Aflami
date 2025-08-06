package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.RemoteCountryDto

fun createRemoteCountryDto(
    englishName: String = "Egypt",
    isoCode: String = "EG",
    nativeName: String = "مصر"
): RemoteCountryDto {
    return RemoteCountryDto(
        englishName = englishName,
        isoCode = isoCode,
        nativeName = nativeName
    )
}