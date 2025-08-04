package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.ProductionCompany
import com.amsterdam.repository.dto.remote.ProductionCompanyDto

fun ProductionCompanyDto.toProductionCompanyEntity(): ProductionCompany {
    return ProductionCompany(
        id = this.id,
        imageUrl = this.fullLogoPath.orEmpty(),
        name = this.name,
        country = this.originCountry
    )
}

fun List<ProductionCompanyDto>.toProductionCompanyEntityList(): List<ProductionCompany> =
    map { it.toProductionCompanyEntity() }