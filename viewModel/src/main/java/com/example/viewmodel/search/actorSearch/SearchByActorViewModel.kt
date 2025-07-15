package com.example.viewmodel.search.actorSearch

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.entity.Movie
import com.example.viewmodel.BaseViewModel
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

    private val queryFlow = MutableStateFlow("")

    init {
        observeQueryFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeQueryFlow() {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .map(String::trim)
                .filter(String::isNotBlank)
                .collectLatest(::onSearchMoviesByActor)
        }
    }

    fun onSearchMoviesByActor(query: String) {
        tryToExecute(
            action = { getMoviesByActorUseCase(query) },
            onSuccess = { result -> updateSearchByActorResult(result) },
            onError = { msg ->
                updateState {
                    it.copy(
                        isLoading = false,
                        movies = emptyList()
                    )
                }
                when (msg) {
                    is NetworkException -> sendNewEffect(SearchByActorEffect.NoInternetConnection)
                }
            }
        )
    }

    override fun onKeywordValueChanged(keyword: String) {
        queryFlow.update { oldText -> keyword }
        updateState { it.copy(query = keyword, isLoading = keyword.isNotBlank()) }
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

    override fun onRetryQuestClicked() {
        updateState { it.copy(isLoading = true) }
        observeQueryFlow()
    }
}