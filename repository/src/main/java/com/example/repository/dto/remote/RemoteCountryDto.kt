package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCountryDto(
    @SerialName("english_name")
    val englishName: String,
    @SerialName("iso_3166_1")
    val isoCode: String,
    @SerialName("native_name")
    val nativeName: String
)