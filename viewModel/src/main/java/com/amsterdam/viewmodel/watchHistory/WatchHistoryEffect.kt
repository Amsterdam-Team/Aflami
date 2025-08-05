package com.amsterdam.viewmodel.watchHistory

interface WatchHistoryEffect {
    data class NavigateToMovieDetails(val movieId: Long) : WatchHistoryEffect
    data class NavigateToTvShowDetails(val tvShowId: Long) : WatchHistoryEffect
    object NavigateBack : WatchHistoryEffect
}