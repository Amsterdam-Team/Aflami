package com.example.viewmodel.search.actorSearch

import com.example.viewmodel.shared.MediaItemUiState

data class ActorSearchUiState(
    val isLoading: Boolean = false,
    val keyword: String = "",
    val movies: List<MediaItemUiState> = emptyList(),
)