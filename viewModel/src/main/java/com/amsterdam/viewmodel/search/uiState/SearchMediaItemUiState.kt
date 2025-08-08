package com.amsterdam.viewmodel.search.uiState

import com.amsterdam.viewmodel.shared.uiStates.MediaType

data class SearchMediaItemUiState(
    val id: Long = 0,
    val name: String = "",
    val posterImageUrl: String = "",
    val yearOfRelease: String = "",
    val rate: String = "",
    val mediaType: MediaType = MediaType.MOVIE
)