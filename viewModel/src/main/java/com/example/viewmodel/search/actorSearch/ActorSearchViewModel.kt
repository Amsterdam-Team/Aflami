package com.example.viewmodel.search.actorSearch

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.entity.Movie
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.search.mapper.toMoveUiStates
import com.example.viewmodel.utils.debounceSearch
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ActorSearchViewModel(
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ActorSearchUiState, ActorSearchEffect>(
    ActorSearchUiState(),
    dispatcherProvider
),
    SearchByActorInteractionListener {

    private val _keyword = MutableStateFlow("")

    init {
        observeQueryFlow()
    }

    private fun observeQueryFlow() {
        viewModelScope.launch { _keyword.debounceSearch(::onSearchMoviesByActor) }
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
                    is NetworkException -> sendNewEffect(ActorSearchEffect.NoInternetConnection)
                }
            }
        )
    }

    override fun onUserSearch(query: String) {
        _keyword.update { oldText -> query }
        updateState { it.copy(keyword = query, isLoading = query.isNotBlank()) }
    }

    private fun updateSearchByActorResult(movies: List<Movie>) {
        updateState {
            it.copy(
                movies = movies.toMoveUiStates(),
                isLoading = false
            )
        }
    }

    override fun onNavigateBackClicked() {
        sendNewEffect(ActorSearchEffect.NavigateBack)
    }

    override fun onRetryQuestClicked() {
        updateState { it.copy(isLoading = true) }
        observeQueryFlow()
    }
}