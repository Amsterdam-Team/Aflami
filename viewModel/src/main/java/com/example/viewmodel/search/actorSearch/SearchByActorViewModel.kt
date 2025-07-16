package com.example.viewmodel.search.actorSearch

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.entity.Movie
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
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

    private val _keyword = MutableStateFlow("")

    init {
        observeKeywordFlow()
    }

    private fun observeKeywordFlow() {
        viewModelScope.launch {
            _keyword
                .debounce(DEBOUNCE_DURATION)
                .map(String::trim)
                .filter(String::isNotBlank)
                .collectLatest(::getActorsMoviesByKeyword)
        }
    }

    private fun getActorsMoviesByKeyword(keyword: String) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getMoviesByActorUseCase(keyword) },
            onSuccess = ::updateSearchByActorResult,
            onError = ::searchMoviesByActorError
        )
    }

    private fun searchMoviesByActorError(message: AflamiException) {
        updateState {
            it.copy(
                isLoading = false,
                movies = emptyList()
            )
        }
        when (message) {
            is NetworkException -> updateState {
                it.copy(
                    isLoading = false,
                    movies = emptyList(),
                    noInternetException = true
                )
            }
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

    override fun onKeywordValueChanged(keyword: String) {
        _keyword.update { keyword }
        updateState { it.copy(keyword = keyword, isLoading = keyword.isNotBlank()) }
    }

    override fun onNavigateBackClicked() {
        sendNewEffect(SearchByActorEffect.NavigateBack)
    }

    override fun onRetryQuestClicked() {
        updateState { it.copy(isLoading = true) }
        observeKeywordFlow()
    }

    companion object {
        private const val DEBOUNCE_DURATION = 300L
    }
}