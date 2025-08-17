package com.amsterdam.viewmodel.continueWatching

import androidx.annotation.BoolRes
import androidx.paging.PagingData
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class ContinueWatchingUiState(
    val continueMediaItemUiStates: Flow<PagingData<ContinueWatchingItemUiState>> = emptyFlow(),
    val isLoading: Boolean = false,
    val error: ContinueWatchingError? = null
) {
    sealed class ContinueWatchingError {
        data object NetworkError : ContinueWatchingError()
    }

    data class ContinueWatchingItemUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val yearOfRelease: String = "",
        val rate: String = "",
        val dateAdded: Instant = Clock.System.now(),
        val mediaType: MediaType = MediaType.MOVIE,
        val isAdult: Boolean = false
    )
}