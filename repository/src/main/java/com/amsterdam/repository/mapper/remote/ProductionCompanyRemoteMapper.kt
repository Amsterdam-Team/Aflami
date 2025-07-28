package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.ProductionCompany
import com.amsterdam.repository.dto.remote.ProductionCompanyDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class ProductionCompanyRemoteMapper @Inject constructor():
    EntityMapper<ProductionCompanyDto, ProductionCompany> {
    override fun toEntity(dto: ProductionCompanyDto): ProductionCompany {
        return ProductionCompany(
            id = dto.id,
            imageUrl = dto.fullLogoPath.orEmpty(),
            name = dto.name,
            country = dto.originCountry
        )
    }
}