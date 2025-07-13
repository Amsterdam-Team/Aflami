package com.amsterdam.repository.dto.remote

import kotlinx.serialization.Serializable

@Serializable
data class RemoteCategoryDto(
    val id: Long,
    val name: String
)