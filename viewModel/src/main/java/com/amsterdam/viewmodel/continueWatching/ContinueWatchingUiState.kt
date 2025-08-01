package com.amsterdam.viewmodel.continueWatching

import androidx.paging.PagingData
import com.amsterdam.viewmodel.home.HomeUiState.ContinueWatchingMediaItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
data class ContinueWatchingUiState(
    val continueMediaItemUiStates:  Flow<PagingData<ContinueWatchingMediaItemUiState>> = emptyFlow(),
    val isLoading: Boolean = false,
    val error: ContinueWatchingError? = null
) {
    sealed class ContinueWatchingError {
        data object NetworkError : ContinueWatchingError()
    }
}