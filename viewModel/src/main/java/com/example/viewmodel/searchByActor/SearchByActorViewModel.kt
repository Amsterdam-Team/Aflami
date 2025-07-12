package com.example.viewmodel.searchByActor

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetMoviesByActorUseCase
import com.example.entity.Movie
import com.example.viewmodel.BaseViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.NothingSerializer

@OptIn(FlowPreview::class)
class SearchByActorViewModel(
     private val getMoviesByActorUseCase: GetMoviesByActorUseCase
) : BaseViewModel<SearchByActorUiState, SearchByActorEffect>(SearchByActorUiState()) {

    init {
        viewModelScope.launch {
            state
                .map { it.query }
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        getMoviesByActor(query)
                    }
                }
        }
    }


    fun onQueryChange(query: String) {
        updateState { it.copy(query = query) }
    }

    private fun getMoviesByActor(query: String) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                getMoviesByActorUseCase.invoke(query)
            },
            onSuccess = { result ->
                updateSearchByActorResult(result)
            },
            onError = {
                     sendNewEffect(SearchByActorEffect.NoInternetConnection)
            }
        )
    }

    private fun updateSearchByActorResult(movies: List<Movie>) {
        updateState { it.copy(movies =movies, isLoading = false) }
    }

    fun onBackClicked() {
        sendNewEffect(SearchByActorEffect.NavigateBack)
    }
}

