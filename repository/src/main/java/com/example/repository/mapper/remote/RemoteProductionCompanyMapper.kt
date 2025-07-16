package com.example.repository.mapper.remote

import com.example.entity.ProductionCompany
import com.example.repository.BuildConfig
import com.example.repository.dto.remote.ProductionCompanyResponse

class RemoteProductionCompanyMapper {
    fun mapProductionCompanyToDomain(productionsCompanies: ProductionCompanyResponse): List<ProductionCompany> =
        productionsCompanies.productionCompanies.map {
            ProductionCompany(
                id = it.id,
                image = BuildConfig.BASE_IMAGE_URL+it.logoPath,
                name = it.name,
                country = it.originCountry
            )
        }

}