package com.example.repository.mapper.local

import com.example.entity.Country
import com.example.repository.dto.local.LocalCountryDto
import com.example.repository.mapper.shared.DtoMapper
import com.example.repository.mapper.shared.EntityMapper

class CountryLocalMapper : EntityMapper<LocalCountryDto, Country>,
    DtoMapper<Country, LocalCountryDto> {

    override fun toEntity(dto: LocalCountryDto): Country {
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