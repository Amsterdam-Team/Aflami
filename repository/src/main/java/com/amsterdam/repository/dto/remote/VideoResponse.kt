package com.amsterdam.repository.dto.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class VideoResponse(
    @SerialName("results")
    val results: List<VideoDto>
)