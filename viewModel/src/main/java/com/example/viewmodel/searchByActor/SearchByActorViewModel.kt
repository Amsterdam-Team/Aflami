package com.example.viewmodel.searchByActor

import com.example.entity.Movie
import com.example.viewmodel.BaseViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
class SearchByActorViewModel(
    // private val getSearchByActorUseCase: GetSearchByActorUseCase
) : BaseViewModel<SearchByActorUiState, SearchByActorEffect>(SearchByActorUiState()) {

    init {
        tryToExecute(
            action = {
                state
                    .debounce(300)
                    .collect { state ->
                        if (state.query.isNotBlank()) {
                            getMoviesByActor(state.query)
                        }
                    }
            },
            onSuccess = { result ->
                // updateSearchByActorResult(result)
            },
            onError = {

            }
        )
    }

    fun onQueryChange(query: String) {
        updateState { it.copy(query = query) }
    }

    private fun getMoviesByActor(query: String) {
        tryToExecute(
            action = {
                // getSearchByActorUseCase.invoke(query)
            },
            onSuccess = { result ->
                //  updateSearchByActorResult(result)
            },
            onError = {

            }
        )
    }

    private fun updateSearchByActorResult(movies: List<Movie>) {
        updateState { it.copy(movies =movies) }
    }

    fun onBackClicked() {
        sendNewEffect(SearchByActorEffect.NavigateBack)
    }
}

