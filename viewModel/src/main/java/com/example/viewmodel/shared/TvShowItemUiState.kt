package com.example.viewmodel.shared

data class TvShowItemUiState(
    val id: Long = 0,
    val name: String = "",
    val posterImageUrl: String = "",
    val yearOfRelease: String = "",
    val rate: String = ""
)