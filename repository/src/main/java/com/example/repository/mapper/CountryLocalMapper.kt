package com.example.repository.mapper

import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto

class CountryLocalMapper {

    fun mapFromLocal(dto: LocalCountryDto): Country {
        return Country(
            countryName = dto.name,
            countryIsoCode = dto.isoCode
        )
    }

    fun mapToLocal(domain: Country): LocalCountryDto {
        return LocalCountryDto(
            name = domain.countryName,
            isoCode = domain.countryIsoCode
        )
    }
}