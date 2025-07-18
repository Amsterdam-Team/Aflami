package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteActorSearchResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val actors: List<ActorSearchItemDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)