package com.amsterdam.viewmodel.myRating

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.TabOption

data class MyRatingUiState(
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val movies: List<RatingMovieUiState> = emptyList(),
    val tvShows: List<RatingTvShowUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isRetryLoading: Boolean = false,
    val error: MyRatingErrorState? = null,
) {
    data class RatingTvShowUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val yearOfRelease: String = "",
        val rate: String = "",
        val isAdult: Boolean = false
    )

    data class RatingMovieUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val yearOfRelease: String = "",
        val rate: String = "",
        val isAdult: Boolean = false
    )

    sealed interface MyRatingErrorState {
        data object RatedMediaFetchError : MyRatingErrorState
        data object NoInternetError : MyRatingErrorState
        data object UnknownError : MyRatingErrorState

        companion object {
            fun mapToUiState(exception: AflamiException): MyRatingErrorState {
                return when (exception) {
                    is NetworkException -> NoInternetError
                    else -> RatedMediaFetchError
                }
            }
        }
    }
}