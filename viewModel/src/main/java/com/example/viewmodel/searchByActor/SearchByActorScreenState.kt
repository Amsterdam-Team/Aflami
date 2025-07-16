package com.example.viewmodel.searchByActor

import androidx.paging.PagingData
import com.example.viewmodel.search.countrySearch.MovieUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchByActorScreenState(
    val isLoading:Boolean = false,
    val query:String="",
    val movies: Flow<PagingData<MovieUiState>> = emptyFlow(),
)
