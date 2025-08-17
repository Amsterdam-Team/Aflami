package com.amsterdam.viewmodel.watchHistory

import androidx.paging.PagingData
import com.amsterdam.viewmodel.shared.TabOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class WatchHistoryUiState(
    val movies: Flow<PagingData<WatchHistoryMovieUiState>> = emptyFlow(),
    val tvShows: Flow<PagingData<WatchHistoryTvShowUiState>> = emptyFlow(),
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val isLoading: Boolean = false,
    val error: WatchHistoryError? = null
) {
    data class WatchHistoryMovieUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val yearOfRelease: String = "",
        val rate: String = "",
        val isAdult: Boolean = false
    )

    data class WatchHistoryTvShowUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val yearOfRelease: String = "",
        val rate: String = "",
        val isAdult: Boolean = false
    )

    sealed class WatchHistoryError {
        data object NetworkError : WatchHistoryError()
    }
}