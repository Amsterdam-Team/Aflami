package com.example.viewmodel.common

data class MediaItemUiState(
    val name: String = "",
    val posterImage: String = "",
    val mediaType: MediaType = MediaType.MOVIE,
    val yearOfRelease: String = "",
    val rate: String = ""

)

enum class MediaType() {
    MOVIE,
    TV_SHOW,
}