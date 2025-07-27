package com.amsterdam.viewmodel.shared.uiStates

data class MovieItemUiState(
    val id: Long = 0,
    val name: String = "",
    val posterImageUrl: String = "",
    val yearOfRelease: String = "",
    val rate: String = "",
    val categories: List<String> = emptyList()

)