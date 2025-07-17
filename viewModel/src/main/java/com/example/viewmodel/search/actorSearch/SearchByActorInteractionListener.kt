package com.example.viewmodel.search.actorSearch

interface SearchByActorInteractionListener {
    fun onUserSearchChange(query : String)
    fun onNavigateBackClick()
    fun onRetrySearchClick()
    fun onMovieClicked(movieId : Long)
}