package com.example.viewmodel.topRated

interface TopRatedEffect {
    data class NavigateToMovieDetailsScreen(val movieId : Long) : TopRatedEffect
    object NavigateBack : TopRatedEffect
}