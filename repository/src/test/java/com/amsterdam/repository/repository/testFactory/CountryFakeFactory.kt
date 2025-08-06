package com.amsterdam.repository.repository.testFactory

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.LocalCountryDto
import com.amsterdam.repository.dto.remote.RemoteCountryDto

val testLanguage = "en"

val testRemoteCountryDto = RemoteCountryDto(
    englishName = "United States",
    isoCode = "US",
    nativeName = "الولايات المتحدة"
)
val testLocalCountryDto = LocalCountryDto(
    isoCode = "US",
    name = "United States",
    storedLanguage = testLanguage
)
val expectedCountry = Country(countryName = "United States", countryIsoCode = "US")
