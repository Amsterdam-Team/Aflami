package com.example.repository.dto.remote.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDetailsDto(
    val name: String? = null,
    val username: String,
    @SerialName("avatar_path")
    val avatarPath: String? = null,
    val rating: Float? = null
)