package com.amsterdam.viewmodel.watchHistory

import androidx.paging.PagingData
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class WatchHistoryUiState(
    val movies: Flow<PagingData<WatchHistoryItemUiState>> = emptyFlow(),
    val tvShows: Flow<PagingData<WatchHistoryItemUiState>> = emptyFlow(),
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val isLoading: Boolean = false,
    val error: WatchHistoryError? = null
) {
    data class WatchHistoryItemUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val yearOfRelease: String = "",
        val rate: String = "",
        val mediaType: MediaType = MediaType.MOVIE
    )
    sealed class WatchHistoryError {
        data object NetworkError : WatchHistoryError()
    }
}