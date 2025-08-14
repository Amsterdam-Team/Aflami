package com.amsterdam.repository.dto.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class VideoRemoteResponse(
    @SerialName("results")
    val results: List<VideoRemoteDto>
)