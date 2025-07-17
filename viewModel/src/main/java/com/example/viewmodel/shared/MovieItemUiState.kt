package com.example.viewmodel.shared

data class MovieItemUiState(
    val id: Long = 0,
    val name: String = "",
    val posterImageUrl: String = "",
    val yearOfRelease: String = "",
    val rate: String = ""
)