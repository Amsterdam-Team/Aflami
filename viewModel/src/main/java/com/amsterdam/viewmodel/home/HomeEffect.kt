package com.amsterdam.viewmodel.home

sealed interface HomeEffect {
    object NavigateToSearchScreenEffect : HomeEffect
    data class NavigateToMovieDetailsEffect(val movieId : Long) : HomeEffect

    data class NavigateToTvShowDetailsEffect(val tvShowId : Long) : HomeEffect
    object NavigateToTopRatedMoviesEffect : HomeEffect
    object NavigateToContinueWatchingMoviesScreen : HomeEffect
}