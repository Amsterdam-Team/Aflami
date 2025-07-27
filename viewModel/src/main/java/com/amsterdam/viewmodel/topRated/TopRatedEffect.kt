package com.amsterdam.viewmodel.topRated

interface TopRatedEffect {
    data class NavigateToMovieDetailsScreen(val movieId: Long) : TopRatedEffect
    data class NavigateToTvShowDetailsEffect(val tvShowId: Long) : TopRatedEffect
    object NavigateBack : TopRatedEffect
}