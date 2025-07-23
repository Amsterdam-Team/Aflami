package com.example.viewmodel.continueWatching

import com.example.viewmodel.shared.uiStates.MovieItemUiState

data class ContinueWatchingUiState(
    val continueWatchingMovies: List<MovieItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: ContinueWatchingError? = null
) {
    sealed class ContinueWatchingError {
        data object NetworkError : ContinueWatchingError()
    }
}