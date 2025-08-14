package com.amsterdam.repository.mapper

import com.amsterdam.entity.ProductionCompany
import com.amsterdam.repository.dto.remote.ProductionCompanyRemoteDto

fun ProductionCompanyRemoteDto.toEntity(): ProductionCompany {
    return ProductionCompany(
        id = this.id,
        imageUrl = this.fullLogoPath.orEmpty(),
        name = this.name,
        country = this.originCountry
    )
}

fun List<ProductionCompanyRemoteDto>.toEntityList(): List<ProductionCompany> =
    map { it.toEntity() }