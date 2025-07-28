package com.amsterdam.viewmodel.search.actorSearch

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.search.mapper.toMediaItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.utils.debounceSearch
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorSearchViewModel @Inject constructor(
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase,
    private val recentSearchesUseCase: RecentSearchesUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ActorSearchUiState, ActorSearchEffect>(
    ActorSearchUiState(),
    dispatcherProvider,
),
    ActorSearchInteractionListener {
    private val keywordFlow = MutableStateFlow("")

    init {
        observeActorSearchQuery()
    }

    private fun observeActorSearchQuery() {
        viewModelScope.launch { keywordFlow.debounceSearch(::executeActorSearch) }
    }

    private fun executeActorSearch(query: String) {
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getMoviesByActorUseCase(
                                query,
                                page,
                            )
                        }
                    },
                )
                    .flow.map { pagingData -> pagingData.map { it.toMediaItemUiState() } }
                    .cachedIn(viewModelScope)
            },
            onSuccess = ::handleSearchResults,
            onError = {},
        )
    }

    override fun onUserSearchChange(keyword: String) {
        keywordFlow.update { keyword }
        updateState { it.copy(keyword = keyword, isLoading = keyword.isNotBlank()) }
    }

    private fun handleSearchResults(movies: Flow<PagingData<MovieItemUiState>>) {
        updateState { it.copy(movies = movies) }
    }

    override fun onClickNavigateBack() {
        onSaveSearchHistory()
        sendNewEffect(ActorSearchEffect.NavigateBack)
    }

    override fun onClickRetrySearch() {
        updateState { it.copy(isLoading = true) }
        executeActorSearch(state.value.keyword)
    }

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(ActorSearchEffect.NavigateToDetailsScreen(movieId))
    }

    override fun onSaveSearchHistory() {
        if (state.value.keyword.isBlank()) return
        tryToExecute(
            action = { recentSearchesUseCase.addRecentSearchForActor(state.value.keyword) },
            onSuccess = { },
            onError = { },
        )
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (val refreshState = loadStates.refresh) {
            is LoadState.Loading -> {
                if (state.value.keyword.isNotBlank()) {
                    updateState { it.copy(isLoading = true, error = null) }
                }
            }

            is LoadState.NotLoading -> {
                updateState { it.copy(isLoading = false) }
            }

            is LoadState.Error -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = ActorSearchErrorState.toActorSearchErrorState(
                            refreshState.error
                        ),
                    )
                }
            }
        }
    }
}
