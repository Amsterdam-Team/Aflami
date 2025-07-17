package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCategoryDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String
)