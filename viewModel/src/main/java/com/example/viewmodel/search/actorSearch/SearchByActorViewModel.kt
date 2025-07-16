package com.example.viewmodel.search.actorSearch

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.entity.Movie
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.globalSearch.mapToSearchUiState
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
                .collectLatest(::onGetActorsMoviesSuccess)
        }
    }

    private fun onGetActorsMoviesSuccess(keyword: String) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getMoviesByActorUseCase(keyword) },
            onSuccess = ::updateSearchByActorResult,
            onError = ::onGetActorsMoviesError
        )
    }

    private fun onGetActorsMoviesError(message: AflamiException) {
        Log.d("actor","${message}  ,,, ${message::class.simpleName}")
        updateState {
            it.copy(
                isLoading = false,
                movies = emptyList(),
                noInternetException = message is NoInternetException
            )
        }
    }
    private fun onFetchError(exception: AflamiException) {
        updateState { it.copy(errorUiState = mapToActorSearchUiState(exception)) }
    }
    private fun updateSearchByActorResult(movies: List<Movie>) {
        updateState {
            it.copy(
                movies = movies.toListOfUiState(),
                isLoading = false,
                noInternetException = false
            )
        }
    }

    override fun onKeywordValueChanged(keyword: String) {
        _keyword.update { keyword }
        if (keyword.isBlank()) {
            updateState { it.copy(
                keyword = "",
                isLoading = false,
                movies = emptyList(),
                noInternetException = false
            ) }
        } else {
            updateState { it.copy(
                keyword = keyword,
                isLoading = true,
                noInternetException = false
            ) }
        }
    }

    override fun onNavigateBackClicked() {
        sendNewEffect(SearchByActorEffect.NavigateBack)
    }

    override fun onRetryQuestClicked() {
        updateState { it.copy(isLoading = true,noInternetException = false) }
        observeKeywordFlow()
    }

    companion object {
        private const val DEBOUNCE_DURATION = 300L
    }
}