package com.example.repository.mapper.remote

import com.example.entity.ProductionCompany
import com.example.repository.BuildConfig
import com.example.repository.dto.remote.ProductionCompanyDto
import com.example.repository.mapper.shared.EntityMapper

class ProductionCompanyRemoteMapper :
    EntityMapper<ProductionCompanyDto, ProductionCompany> {
    override fun toEntity(dto: ProductionCompanyDto): ProductionCompany {
        return ProductionCompany(
            id = dto.id,
            imageUrl = BuildConfig.BASE_IMAGE_URL + dto.logoPath,
            name = dto.name,
            country = dto.originCountry
        )
    }
}