package com.example.viewmodel.shared

data class MediaItemUiState(
    val id: Long = 0,
    val name: String = "",
    val posterImageUrl: String = "",
    val mediaType: MediaType = MediaType.MOVIE,
    val yearOfRelease: String = "",
    val rate: String = ""
)

enum class MediaType {
    MOVIE,
    TV_SHOW,
}