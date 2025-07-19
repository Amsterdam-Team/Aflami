package com.example.viewmodel.search.actorSearch

interface SearchByActorInteractionListener {
    fun onUserSearch(query : String)
    fun onNavigateBackClicked()
    fun onRetryQuestClicked()
    fun onMovieClicked(movieId : Long)
}