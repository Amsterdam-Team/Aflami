package com.amsterdam.viewmodel.myRating

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState

data class MyRatingUiState(
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val movies: List<MovieItemUiState> = emptyList(),
    val tvShows: List<TvShowItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: MyRatingErrorState? = null,
)

sealed interface MyRatingErrorState {
    data object RatedMediaFetchError : MyRatingErrorState
    data object NoInternetError: MyRatingErrorState
    data object UnknownError : MyRatingErrorState

    companion object {
        fun mapToUiState(exception: AflamiException): MyRatingErrorState {
            return when(exception){
                is NetworkException -> NoInternetError
                else -> RatedMediaFetchError
            }
        }
    }
}