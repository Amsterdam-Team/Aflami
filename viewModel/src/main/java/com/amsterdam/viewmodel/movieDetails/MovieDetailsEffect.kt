package com.amsterdam.viewmodel.movieDetails

sealed interface MovieDetailsEffect {
    data object NavigateBackEffect : MovieDetailsEffect
    data object NavigateToCastsScreenEffect : MovieDetailsEffect
    data object NavigateToLoginScreenEffect : MovieDetailsEffect
    data class NavigateToMovieDetails(val movieId: Long) : MovieDetailsEffect
    data class LaunchMovieVideoEffect(val url: String) : MovieDetailsEffect

    data object MovieAddedToListSuccessfully : MovieDetailsEffect
    data object MovieAddedToListError : MovieDetailsEffect
    data object ListCreatedSuccessfully : MovieDetailsEffect

    data object FailedToCreateList : MovieDetailsEffect
    data object ShowRatingSuccessSnackBar : MovieDetailsEffect
    data object ShowRatingErrorSnackBar : MovieDetailsEffect
}
