package com.example.viewmodel.search.actorSearch

sealed interface SearchActorEffect{
    data object NavigateBack:SearchActorEffect
    data object NavigateToDetailsScreen : SearchActorEffect
}