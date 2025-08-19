package com.amsterdam.viewmodel.search.actorSearch

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.paging.createPagingSource
import com.amsterdam.viewmodel.search.mapper.toSearchMediaItemUiState
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.debounceSearch
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ActorSearchViewModel @Inject constructor(
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ActorSearchUiState, ActorSearchEffect>(
    ActorSearchUiState(),
    dispatcherProvider,
), ActorSearchInteractionListener {
    init {
        observeKeywordFlow()
    }

    @OptIn(FlowPreview::class)
    private fun observeKeywordFlow() {
        tryToExecute(
            action = {
                state.map { it -> it.keyword.trim() }.debounceSearch(::executeActorSearch)
            },
            dispatcher = dispatcherProvider.IO
        )
    }

    private fun executeActorSearch(query: String) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                createPagingSource(scope = viewModelScope) { page ->
                    getMoviesByActorUseCase(query, page).map { it.toSearchMediaItemUiState() }
                }
            },
            onSuccess = ::handleSearchResults
        )
    }

    override fun onUserSearchChange(keyword: String) {
        updateState { it.copy(keyword = keyword) }
    }

    private fun handleSearchResults(movies: Flow<PagingData<SearchMediaItemUiState>>) {
        updateState { it.copy(movies = movies) }
    }

    override fun onClickNavigateBack() {
        sendNewNavigationEffect(ActorSearchEffect.NavigateBack)
    }

    override fun onClickRetrySearch() {
        updateState { it.copy(isLoading = true) }
        executeActorSearch(state.value.keyword)
    }

    override fun onClickMovie(movieId: Long) {
        sendNewNavigationEffect(ActorSearchEffect.NavigateToDetailsScreen(movieId))
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (loadStates.refresh) {
            is LoadState.Loading -> {
                if (state.value.keyword.isNotBlank()) {
                    resetErrorStateToNull()
                    updateState { it.copy(isLoading = true) }
                }
            }

            is LoadState.NotLoading -> updateState { it.copy(isLoading = false) }

            is LoadState.Error -> {
                updateState { it.copy(isLoading = false) }
                updateErrorStateByException(NetworkException())
            }
        }
    }
}
