package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.LocalCountryDto
import com.amsterdam.repository.mapper.shared.DtoMapper
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class CountryLocalMapper @Inject constructor(): EntityMapper<LocalCountryDto, Country>,
    DtoMapper<Country, LocalCountryDto> {

    override fun toEntity(dto: LocalCountryDto): Country {
        return Country(
            countryName = dto.name,
            countryIsoCode = dto.isoCode
        )
    }

    override fun toDto(entity: Country, args: List<Any>): LocalCountryDto {
        return LocalCountryDto(
            name = entity.countryName,
            storedLanguage = args.first().toString(),
            isoCode = entity.countryIsoCode,
        )
    }
}