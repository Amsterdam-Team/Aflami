package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompanyRemoteResponse(
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompanyRemoteDto>
)
