package com.example.repository.dto.remote.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionResponseDto(
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("success")
    val success: Boolean,
)
