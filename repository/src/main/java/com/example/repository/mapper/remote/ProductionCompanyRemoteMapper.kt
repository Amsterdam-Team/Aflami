package com.example.repository.mapper.remote

import com.example.domain.mapper.DomainMapper
import com.example.entity.ProductionCompany
import com.example.repository.BuildConfig
import com.example.repository.dto.remote.ProductionCompanyResponse

class ProductionCompanyRemoteMapper: DomainMapper<List<ProductionCompany>, ProductionCompanyResponse> {

    override fun toDomain(dto: ProductionCompanyResponse): List<ProductionCompany> {
       return dto.productionCompanies.map {
            ProductionCompany(
                id = it.id,
                imageUrl = BuildConfig.BASE_IMAGE_URL + it.logoPath,
                name = it.name,
                country = it.originCountry
            )
        }
    }
}