package com.example.viewmodel.searchByActor

sealed interface SearchByActorEffect{

    data class ShowError(val message:String):SearchByActorEffect
        object NavigateToDetails:SearchByActorEffect
        object NoResultsFound:SearchByActorEffect

}