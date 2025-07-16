package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompanyDto(
    @SerialName("id")
    val id: Long,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("name")
    val name: String,
    @SerialName("origin_country")
    val originCountry: String
)