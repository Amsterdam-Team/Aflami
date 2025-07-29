package com.amsterdam.viewmodel.topRated

import androidx.paging.PagingData
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TopRatedUiState(
    val movies: Flow<PagingData<MovieItemUiState>> = emptyFlow(),
    val isLoading: Boolean = false,
    val error: TopRatedError? = null
) {
    sealed class TopRatedError {
        data object NetworkError : TopRatedError()
    }
}