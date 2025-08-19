package com.amsterdam.viewmodel.search.actorSearch

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.search.mapper.toSearchMediaItemUiState
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.debounceSearch
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorSearchViewModel @Inject constructor(
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ActorSearchUiState, ActorSearchEffect>(
    ActorSearchUiState(),
    dispatcherProvider,
), ActorSearchInteractionListener {

    private val keywordFlow = MutableStateFlow("")

    init {
        manageLocaleLanguageUseCase.getAppLanguage()
            .onEach {
                observeActorSearchQuery()
            }.launchIn(viewModelScope)

        observeActorSearchQuery()
    }

    private fun observeActorSearchQuery() {
        viewModelScope.launch { keywordFlow.debounceSearch(::executeActorSearch) }
    }

    private fun executeActorSearch(query: String) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getMoviesByActorUseCase(query, page)
                                .map { it.toSearchMediaItemUiState() }
                        }
                    },
                ).flow.cachedIn(viewModelScope)
            },
            onSuccess = ::handleSearchResults
        )
    }

    override fun onUserSearchChange(keyword: String) {
        if (keyword.trim() != state.value.keyword.trim()) {
            keywordFlow.update { keyword }
        }
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
