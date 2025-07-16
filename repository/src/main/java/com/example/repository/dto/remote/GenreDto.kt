package com.example.repository.dto.remote

import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    val id: Int = 0,
    val name: String = ""
)