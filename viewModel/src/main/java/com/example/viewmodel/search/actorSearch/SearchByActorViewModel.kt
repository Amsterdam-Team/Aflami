package com.example.viewmodel.search.actorSearch

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.entity.Movie
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.actorSearch.SearchByActorScreenState.SearchByActorError
import com.example.viewmodel.search.mapper.toListOfUiState
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchByActorViewModel(
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SearchByActorScreenState, SearchByActorEffect>(
    SearchByActorScreenState(),
    dispatcherProvider
),
    SearchByActorInteractionListener {

    private val keywordFlow = MutableStateFlow("")

    init {
        observeActorSearchQuery()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeActorSearchQuery() {
        viewModelScope.launch {
            keywordFlow
                .debounce(300)
                .map(String::trim)
                .filter(String::isNotBlank)
                .collectLatest(::executeActorSearch)
        }
    }

    private fun executeActorSearch(query: String) {
        tryToExecute(
            action = { getMoviesByActorUseCase(query) },
            onSuccess = ::handleSearchResults,
            onError = ::onError
        )
    }

    override fun onUserSearchChange(keyword: String) {
        keywordFlow.update { oldText -> keyword }
        updateState { it.copy(keyword = keyword, isLoading = keyword.isNotBlank()) }
    }

    private fun handleSearchResults(movies: List<Movie>) {
        updateState {
            it.copy(
                movies = movies.toListOfUiState(),
                isLoading = false,
                error = null
            )
        }
    }

    override fun onNavigateBackClick() {
        sendNewEffect(SearchByActorEffect.NavigateBack)
    }

    override fun onRetrySearchClick() {
        updateState { it.copy(isLoading = true) }
        executeActorSearch(state.value.keyword)
    }

    override fun onMovieClicked(movieId: Long) {
        updateState { it.copy(selectedMovieId = movieId) }
        sendNewEffect(SearchByActorEffect.NavigateToDetailsScreen)
    }

    private fun onError(aflamiException: AflamiException) {
        updateState {
            when (aflamiException) {
                is NetworkException -> it.copy(
                    error = SearchByActorError.NetworkError,
                    isLoading = false,
                    movies = emptyList()
                )

                else -> it.copy(
                    isLoading = false,
                    movies = emptyList()
                )
            }
        }
    }
}