package com.example.viewmodel.search.actorSearch

sealed interface ActorSearchEffect{
    data object NavigateBack:ActorSearchEffect
    data object NoInternetConnection:ActorSearchEffect
    object NavigateToMovieDetails : ActorSearchEffect
}