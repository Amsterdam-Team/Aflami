package com.example.viewmodel.search.actorSearch

sealed interface ActorSearchEffect {
    object NavigateBack : ActorSearchEffect
    object NoInternetConnection : ActorSearchEffect
    data class NavigateToMovieDetails(val movieId: Long) : ActorSearchEffect
}