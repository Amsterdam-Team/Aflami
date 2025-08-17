package com.amsterdam.viewmodel.myRating

sealed interface MyRatingUiEffect {
    data object NavigateBack : MyRatingUiEffect
    data class NavigateToMovieDetails(val movieId: Long) : MyRatingUiEffect
    data class NavigateToSeriesDetails(val tvShowId: Long) : MyRatingUiEffect

    data object ShowDeleteRateSuccessSnackBar : MyRatingUiEffect
    data object ShowDeleteRateErrorSnackBar : MyRatingUiEffect
}