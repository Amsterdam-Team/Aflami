package com.amsterdam.viewmodel.home

sealed interface HomeEffect {
    data object NavigateToSearchScreenEffect : HomeEffect
    data class NavigateToMovieDetailsEffect(val movieId: Long) : HomeEffect

    data class NavigateToTvShowDetailsEffect(val tvShowId: Long) : HomeEffect
    data object NavigateToTopRatedMoviesEffect : HomeEffect
    data object NavigateToContinueWatchingMoviesScreen : HomeEffect

    data object ShowGetMoviesByMoodErrorSnackBar : HomeEffect
}