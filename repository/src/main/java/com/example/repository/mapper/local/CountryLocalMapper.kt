package com.example.repository.mapper.local

import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto

class CountryLocalMapper {

    fun mapToCountries(localCountries: List<LocalCountryDto>): List<Country> {
        return localCountries.map { mapToCountry(it) }
    }

    fun mapToCountry(localCountry: LocalCountryDto): Country {
        return Country(
            countryName = localCountry.name,
            countryIsoCode = localCountry.isoCode
        )
    }

    fun mapToLocalCountry(country: Country): LocalCountryDto {
        return LocalCountryDto(
            name = country.countryName,
            isoCode = country.countryIsoCode
        )
    }
}