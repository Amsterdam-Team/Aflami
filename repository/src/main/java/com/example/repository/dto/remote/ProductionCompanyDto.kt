package com.example.repository.dto.remote

import com.example.repository.BuildConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompanyDto(
    @SerialName("id")
    val id:  Long,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("name")
    val name: String,
    @SerialName("origin_country")
    val originCountry: String
){
    val fullLogoPath: String?
        get() = logoPath?.let { BuildConfig.BASE_IMAGE_URL + it }
}