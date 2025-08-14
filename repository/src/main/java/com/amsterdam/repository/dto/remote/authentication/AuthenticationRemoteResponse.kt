package com.amsterdam.repository.dto.remote.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationRemoteResponse(
    @SerialName("success")
    val isSuccess: Boolean = false,
    @SerialName("failure")
    val isFailure: Boolean? = null,
    @SerialName("status_code")
    val statusCode: Int? = null,
    @SerialName("expires_at")
    val expiresAt: String? = null,
    @SerialName("request_token")
    val requestToken: String? = null,
    @SerialName("status_message")
    val statusMessage: String? = null,
    @SerialName("error")
    val error: String? = null,
)
