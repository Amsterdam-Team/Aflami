package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryRemoteResponse(
    @SerialName("genres") val genres: List<CategoryRemoteDto>
)