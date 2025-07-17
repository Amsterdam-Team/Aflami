package com.example.viewmodel.search.actorSearch


interface SearchByActorInteractionListener {
    fun onUserSearch(query : String)
    fun onNavigateBackClick()
    fun onRetrySearchClick()
    fun onMovieClicked(movieId : Long)
}