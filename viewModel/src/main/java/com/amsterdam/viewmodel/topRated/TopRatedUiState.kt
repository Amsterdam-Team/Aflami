package com.amsterdam.viewmodel.topRated

import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState

data class TopRatedUiState(
    val topRatedMediaItems: List<MediaItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: TopRatedError? = null
) {
    sealed class TopRatedError {
        data object NetworkError : TopRatedError()
    }
}