package com.example.viewmodel.searchByActor

import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.entity.Movie
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchByActorViewModel(
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SearchByActorScreenState, SearchByActorEffect>(SearchByActorScreenState(),dispatcherProvider),
    SearchByActorInteractionListener {

    private val queryFlow = MutableStateFlow("")

    init {
        observeQueryFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeQueryFlow() {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    callbackFlow<List<Movie>> {
                        updateState { it.copy(isLoading = true) }
                        val job = tryToExecute(
                            action = { getMoviesByActorUseCase(query) },
                            onSuccess = { result -> trySend(result).isSuccess },
                            onError = {
                                sendNewEffect(SearchByActorEffect.NoInternetConnection)
                                trySend(emptyList()).isSuccess
                            }
                        )
                        awaitClose { job.cancel() }
                    }
                }
                .collect { movies ->
                    updateSearchByActorResult(movies)
                }
        }
    }

    override fun onUserSearch(query: String) {
        queryFlow.value = query
        updateState { it.copy(query = query) }
        if (query.isBlank()) {
            updateState { it.copy(movies = emptyList(), isLoading = false) }
        }
    }

    private fun updateSearchByActorResult(movies: List<Movie>) {
        updateState {
            it.copy(
                movies = movies.toListOfUiState(),
                isLoading = false
            )
        }
    }

    override fun onNavigateBackClicked() {
        sendNewEffect(SearchByActorEffect.NavigateBack)
    }
}