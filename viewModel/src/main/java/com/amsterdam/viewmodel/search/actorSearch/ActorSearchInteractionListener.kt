package com.amsterdam.viewmodel.search.actorSearch

import androidx.paging.CombinedLoadStates

interface ActorSearchInteractionListener {
    fun onUserSearchChange(keyword: String)
    fun onClickNavigateBack()
    fun onClickRetrySearch()
    fun onClickMovie(movieId: Long)
    fun onSaveSearchHistory()
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)
}