package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteUserListResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<RemoteUserListDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

