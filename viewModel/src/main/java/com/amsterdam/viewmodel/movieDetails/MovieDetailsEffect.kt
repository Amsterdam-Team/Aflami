package com.amsterdam.viewmodel.movieDetails

sealed interface MovieDetailsEffect {
    object NavigateBackEffect : MovieDetailsEffect
    object NavigateToCastsScreenEffect : MovieDetailsEffect
    object NavigateToLoginScreenEffect : MovieDetailsEffect
    data class NavigateToMovieDetails(val movieId: Long) : MovieDetailsEffect
    data class LaunchMovieVideoEffect(val url: String) : MovieDetailsEffect
}