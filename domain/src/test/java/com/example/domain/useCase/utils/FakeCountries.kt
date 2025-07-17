package com.example.domain.useCase.utils

import com.example.entity.Country

val fakeCountryList =
    listOf(
        Country(
            countryName = "EGYPT",
            countryIsoCode = "EG",
        ),
        Country(
            countryName = "IRAQ",
            countryIsoCode = "IR",
        ),
    )

val countriesWithDifferentCases =
    listOf(
        Country(countryName = "United States", countryIsoCode = "us"),
        Country(countryName = "Australia", countryIsoCode = "au"),
        Country(countryName = "canada", countryIsoCode = "ca"),
        Country(countryName = "Germany", countryIsoCode = "de"),
    )