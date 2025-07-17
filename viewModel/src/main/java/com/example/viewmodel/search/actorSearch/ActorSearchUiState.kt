package com.example.viewmodel.search.actorSearch

import com.example.viewmodel.shared.MovieItemUiState

data class ActorSearchUiState(
    val isLoading: Boolean = false,
    val keyword: String = "",
    val movies: List<MovieItemUiState> = emptyList(),
)