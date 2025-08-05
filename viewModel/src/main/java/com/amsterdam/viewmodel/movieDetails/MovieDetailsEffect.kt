package com.amsterdam.viewmodel.movieDetails

sealed interface MovieDetailsEffect {
    object NavigateBackEffect : MovieDetailsEffect
    object NavigateToCastsScreenEffect : MovieDetailsEffect
    object NavigateToLoginScreenEffect : MovieDetailsEffect
    data class NavigateToMovieDetails(val movieId: Long) : MovieDetailsEffect
    data class LaunchMovieVideoEffect(val url: String) : MovieDetailsEffect

    object MovieAddedToListSuccessfully : MovieDetailsEffect

    object MovieAddedToListError : MovieDetailsEffect

    object ListCreatedSuccessfully : MovieDetailsEffect

    object FailedToCreateList : MovieDetailsEffect

    object ShowRatingSuccessSnackBar: MovieDetailsEffect
    object ShowRatingErrorSnackBar: MovieDetailsEffect
}
