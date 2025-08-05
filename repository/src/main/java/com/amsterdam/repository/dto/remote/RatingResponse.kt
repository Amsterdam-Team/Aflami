package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingResponse(
    @SerialName("status_code")
    val statusCode: Int? = null,
    @SerialName("status_message")
    val statusMessage: String? = null
)