package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryRemoteDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)