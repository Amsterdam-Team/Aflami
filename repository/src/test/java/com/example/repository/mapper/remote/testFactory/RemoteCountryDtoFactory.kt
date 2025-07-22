package com.example.repository.mapper.remote.testFactory

import com.example.repository.dto.remote.RemoteCountryDto


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