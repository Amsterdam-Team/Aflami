package com.example.viewmodel.search.actorSearch

import com.example.viewmodel.search.countrySearch.CountrySearchEffect

sealed interface ActorSearchEffect{
    data object NavigateBack:ActorSearchEffect
    data object NoInternetConnection:ActorSearchEffect
    object NavigateToMovieDetails : ActorSearchEffect
}