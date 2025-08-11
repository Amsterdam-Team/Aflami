package com.amsterdam.viewmodel.listDetails

import com.amsterdam.viewmodel.listDetails.ListDetailsUiState.ListDetailsError

sealed interface ListDetailsEffect {
    data object NavigateBack : ListDetailsEffect
    data class NavigateToMovieDetailsScreen(val movieId: Long) : ListDetailsEffect
    data class NavigateToTvShowDetailsScreen(val tvShowId: Long): ListDetailsEffect
    data class ShowErrorSnackbar(val error: ListDetailsError?) : ListDetailsEffect
    data object ShowDeletionSuccessSnackBar : ListDetailsEffect
    data object ShowRemoveMovieSuccessSnackBar : ListDetailsEffect
}