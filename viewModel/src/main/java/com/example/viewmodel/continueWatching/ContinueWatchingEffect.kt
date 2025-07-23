package com.example.viewmodel.continueWatching

interface ContinueWatchingEffect {
    data class NavigateToMovieDetailsScreen(val movieId : Long) : ContinueWatchingEffect
    object NavigateBack : ContinueWatchingEffect
}