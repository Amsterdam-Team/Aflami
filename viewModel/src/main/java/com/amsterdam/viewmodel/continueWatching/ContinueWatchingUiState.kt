package com.amsterdam.viewmodel.continueWatching

import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState

data class ContinueWatchingUiState(
    val continueMediaItemUiStates: List<MediaItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: ContinueWatchingError? = null
) {
    sealed class ContinueWatchingError {
        data object NetworkError : ContinueWatchingError()
    }
}