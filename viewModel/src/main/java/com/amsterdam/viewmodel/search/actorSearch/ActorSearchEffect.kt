package com.amsterdam.viewmodel.search.actorSearch

sealed interface ActorSearchEffect{
    data object NavigateBack:ActorSearchEffect
    data class NavigateToDetailsScreen (val movieId : Long) : ActorSearchEffect
}