package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.LocalCountryDto

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