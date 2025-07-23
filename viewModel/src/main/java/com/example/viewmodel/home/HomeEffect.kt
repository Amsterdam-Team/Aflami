package com.example.viewmodel.home

sealed interface HomeEffect {
    object NavigateToSearchScreenEffect : HomeEffect
    data class NavigateToMovieDetailsEffect(val movieId : Long) : HomeEffect
    object NavigateToTopRatedMoviesEffect : HomeEffect
}