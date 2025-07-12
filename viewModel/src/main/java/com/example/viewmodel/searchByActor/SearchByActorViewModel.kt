package com.example.viewmodel.searchByActor

import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.entity.Movie
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
import kotlinx.coroutines.Job

class SearchByActorViewModel(
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase
) : BaseViewModel<SearchByActorScreenState, SearchByActorEffect>(SearchByActorScreenState()),
    SearchByActorInteractionListener {

    var searchJob: Job? = null

    override fun onUserSearch(query: String) {
        updateState { it.copy(query = query, isLoading = true) }
        if (query.isEmpty()) {
            updateState { it.copy(movies = emptyList(), isLoading = false) }
            return
        }
        launchSearchJob()
    }

    private fun launchSearchJob() {
        searchJob?.cancel()
        searchJob = launchDelayed(300L) { this@SearchByActorViewModel.getMoviesByActor() }
    }

    private fun getMoviesByActor() {
        tryToExecute(action = {
            getMoviesByActorUseCase.invoke(state.value.query)
        }, onSuccess = { result ->
            updateSearchByActorResult(result)
        }, onError = {
            sendNewEffect(SearchByActorEffect.NoInternetConnection)
        })
    }

    private fun updateSearchByActorResult(movies: List<Movie>) {
        updateState { it.copy(movies = movies.toListOfUiState(), isLoading = false) }
    }

    override fun onNavigateBackClicked() {
        sendNewEffect(SearchByActorEffect.NavigateBack)
    }
}

