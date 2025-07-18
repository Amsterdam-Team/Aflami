package com.example.viewmodel.search.actorSearch

interface SearchActorInteractionListener {
    fun onUserSearchChange(query : String)
    fun onClickNavigateBack()
    fun onClickRetrySearch()
    fun onClickMovie(movieId : Long)
}