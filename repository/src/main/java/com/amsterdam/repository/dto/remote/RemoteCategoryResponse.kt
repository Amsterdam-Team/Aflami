package com.amsterdam.repository.dto.remote

import kotlinx.serialization.Serializable

@Serializable
data class RemoteCategoryResponse(
    val genres: List<RemoteCategoryDto>
)