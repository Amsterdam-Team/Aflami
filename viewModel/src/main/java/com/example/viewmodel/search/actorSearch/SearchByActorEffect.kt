package com.example.viewmodel.search.actorSearch

sealed interface SearchByActorEffect{

    data object NavigateBack: SearchByActorEffect
}