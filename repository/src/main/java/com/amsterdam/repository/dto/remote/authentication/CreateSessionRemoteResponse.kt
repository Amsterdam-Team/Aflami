package com.amsterdam.repository.dto.remote.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionRemoteResponse(
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("success")
    val success: Boolean,
)
