package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RemotePeopleResponse(
    val page: Int,
    val results: List<RemotePeopleItemDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)