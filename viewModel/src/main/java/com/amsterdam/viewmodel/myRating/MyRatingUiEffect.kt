package com.amsterdam.viewmodel.myRating

import com.amsterdam.viewmodel.movieDetails.MovieDetailsEffect

sealed interface MyRatingUiEffect {
    object NavigateBack : MyRatingUiEffect
    data class NavigateToMovieDetails(val movieId: Long) : MyRatingUiEffect
    data class NavigateToSeriesDetails(val tvShowId: Long) : MyRatingUiEffect

    object ShowDeleteRateSuccessSnackBar: MyRatingUiEffect
    object ShowDeleteRateErrorSnackBar: MyRatingUiEffect
}