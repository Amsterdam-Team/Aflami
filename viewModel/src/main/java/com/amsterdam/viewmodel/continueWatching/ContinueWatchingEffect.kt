package com.amsterdam.viewmodel.continueWatching

interface ContinueWatchingEffect {
    data class NavigateToMovieDetailsScreen(val movieId: Long) : ContinueWatchingEffect
    data class NavigateToTvShowDetailsEffect(val tvShowId: Long) : ContinueWatchingEffect
    object NavigateBack : ContinueWatchingEffect
}