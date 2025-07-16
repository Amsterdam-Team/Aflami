package com.example.viewmodel.searchByActor

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.paging.PagingSource
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.search.countrySearch.MovieUiState
import com.example.viewmodel.search.mapper.toUiState
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
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
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getMoviesByActorUseCase(query, page)
                        }
                    },
                ).flow.map { pagingData -> pagingData.map { it.toUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = { result -> updateSearchByActorResult(result) },
            onError = { msg ->
                updateState {
                    it.copy(
                        isLoading = false,
                        movies = emptyFlow(),
                    )
                }
                when (msg) {
                    is NetworkException -> sendNewEffect(SearchByActorEffect.NoInternetConnection)
                }
            },
        )
    }

    override fun onUserSearch(query: String) {
        queryFlow.update { oldText -> query }
        updateState { it.copy(query = query, isLoading = query.isNotBlank()) }
    }

    private fun updateSearchByActorResult(movies: Flow<PagingData<MovieUiState>>) {
        updateState {
            it.copy(
                movies = movies,
                isLoading = false,
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