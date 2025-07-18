package com.example.repository.mapper.local

import com.example.domain.mapper.DomainMapper
import com.example.domain.mapper.DtoMapper
import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto

class CountryLocalMapper: DomainMapper<Country, LocalCountryDto> ,DtoMapper<Country, LocalCountryDto> {

    override fun toDomain(dto: LocalCountryDto): Country {
        return Country(
            countryName = dto.name,
            countryIsoCode = dto.isoCode
        )
    }

    override fun toDto(domain: Country): LocalCountryDto {
        return LocalCountryDto(
            name = domain.countryName,
            isoCode = domain.countryIsoCode
        )
    }
}