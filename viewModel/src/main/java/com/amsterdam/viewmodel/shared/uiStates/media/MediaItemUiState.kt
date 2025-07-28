package com.amsterdam.viewmodel.shared.uiStates.media

data class MediaItemUiState(
    val id: Long = 0,
    val name: String = "",
    val posterImageUrl: String = "",
    val yearOfRelease: String = "",
    val rate: String = "",
    val mediaType: MediaType = MediaType.MOVIE
)