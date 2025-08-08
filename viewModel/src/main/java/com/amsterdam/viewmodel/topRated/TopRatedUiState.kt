package com.amsterdam.viewmodel.topRated

import androidx.paging.PagingData
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TopRatedUiState(
    val mediaItems: Flow<PagingData<TopRatedMediaItemUiState>> = emptyFlow(),
    val isLoading: Boolean = false,
    val error: TopRatedError? = null
) {
    class TopRatedMediaItemUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val yearOfRelease: String = "",
        val rate: String = "",
        val mediaType : MediaType = MediaType.MOVIE
    )

    sealed class TopRatedError {
        data object NetworkError : TopRatedError()
    }
}