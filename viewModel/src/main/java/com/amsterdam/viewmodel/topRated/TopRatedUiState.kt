package com.amsterdam.viewmodel.topRated

import androidx.paging.PagingData
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TopRatedUiState(
    val mediaItems: Flow<PagingData<MediaItemUiState>> = emptyFlow(),
    val isLoading: Boolean = false,
    val error: TopRatedError? = null
) {
    sealed class TopRatedError {
        data object NetworkError : TopRatedError()
    }
}