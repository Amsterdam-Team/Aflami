package com.example.repository.dto.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProductionCompanyResponse(
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompanyDto>
)
