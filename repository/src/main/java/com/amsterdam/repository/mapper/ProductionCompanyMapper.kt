package com.amsterdam.repository.mapper

import com.amsterdam.entity.ProductionCompany
import com.amsterdam.repository.dto.remote.ProductionCompanyDto

fun ProductionCompanyDto.toEntity(): ProductionCompany {
    return ProductionCompany(
        id = this.id,
        imageUrl = this.fullLogoPath.orEmpty(),
        name = this.name,
        country = this.originCountry
    )
}

fun List<ProductionCompanyDto>.toEntityList(): List<ProductionCompany> =
    map { it.toEntity() }