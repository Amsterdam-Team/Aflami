package com.amsterdam.repository.dto.remote

import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompanyRemoteDto(
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
        get() = logoPath?.let { BASE_IMAGE_URL_W500 + it }
}