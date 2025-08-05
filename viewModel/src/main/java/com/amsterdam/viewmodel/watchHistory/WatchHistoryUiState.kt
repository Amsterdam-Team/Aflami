package com.amsterdam.viewmodel.watchHistory

import androidx.paging.PagingData
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class WatchHistoryUiState(
    val movies: Flow<PagingData<MovieItemUiState>> = emptyFlow(),
    val tvShows: Flow<PagingData<TvShowItemUiState>> = emptyFlow(),
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val isLoading: Boolean = false,
    val error: WatchHistoryError? = null
) {
    sealed class WatchHistoryError {
        data object NetworkError : WatchHistoryError()
    }
}