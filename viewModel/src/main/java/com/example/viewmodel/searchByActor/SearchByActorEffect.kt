package com.example.viewmodel.searchByActor

sealed interface SearchByActorEffect{
    data object NavigateBack:SearchByActorEffect
    data object NoInternetConnection:SearchByActorEffect
}