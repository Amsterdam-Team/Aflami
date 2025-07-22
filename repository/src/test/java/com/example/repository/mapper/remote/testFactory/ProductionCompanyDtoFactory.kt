package com.example.repository.mapper.remote.testFactory

import com.example.repository.dto.remote.ProductionCompanyDto

fun createProductionCompanyDto(
    id: Long = 0L,
    logoPath: String? = "/default-logo.png",
    name: String = "Default Studio",
    originCountry: String = "US"
): ProductionCompanyDto {
    return ProductionCompanyDto(
        id = id,
        logoPath = logoPath,
        name = name,
        originCountry = originCountry
    )
}