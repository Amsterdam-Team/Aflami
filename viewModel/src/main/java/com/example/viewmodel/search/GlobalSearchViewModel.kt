package com.example.viewmodel.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.useCase.GetMoviesByKeywordUseCase
import com.example.domain.useCase.GetTvShowByKeywordUseCase
import com.example.domain.useCase.search.AddRecentSearchUseCase
import com.example.domain.useCase.search.ClearAllRecentSearchesUseCase
import com.example.domain.useCase.search.ClearRecentSearchUseCase
import com.example.domain.useCase.search.GetRecentSearchesUseCase
import com.example.entity.Movie
import com.example.entity.TvShow
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.common.GenreItemUiState.Companion.getSelectedOne
import com.example.viewmodel.common.GenreItemUiState.Companion.selectByType
import com.example.viewmodel.common.GenreType
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.common.toMoveUiStates
import com.example.viewmodel.common.toTvShowUiStates
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class GlobalSearchViewModel(
    private val getMoviesByKeywordUseCase: GetMoviesByKeywordUseCase,
    private val getTvShowByKeywordUseCase: GetTvShowByKeywordUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val addRecentSearchUseCase: AddRecentSearchUseCase,
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase,
    private val clearAllRecentSearchesUseCase: ClearAllRecentSearchesUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SearchUiState, SearchUiEffect>(SearchUiState(), dispatcherProvider),
    GlobalSearchInteractionListener, FilterInteractionListener {

    private val queryFlow = MutableStateFlow(state.value.query)

    init {
        loadRecentSearches()
        observeQueryFlow()
    }

    private fun loadRecentSearches() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getRecentSearchesUseCase() },
            onSuccess = ::onLoadRecentSearchesSuccess,
            onError = ::onFetchError,
        )
    }

    private fun onLoadRecentSearchesSuccess(recentSearches: List<String>) {
        updateState { it.copy(recentSearches = recentSearches, isLoading = false) }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeQueryFlow() {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .filter { it.isNotBlank() }
                .flatMapLatest { query ->
                    callbackFlow {
                        val job = tryToExecute(
                            action = { onSearchQueryChanged(query) },
                            onSuccess = { result -> trySend(result).isSuccess },
                            onError = { }
                        )
                        awaitClose { job.cancel() }
                    }
                }
                .collect {  }
        }
    }

    private fun onSearchQueryChanged(trimmedQuery: String) {
        when (state.value.selectedTabOption) {
            TabOption.MOVIES -> fetchMoviesByQuery(trimmedQuery)
            TabOption.TV_SHOWS -> fetchTvShowsByQuery(trimmedQuery)
        }
    }

    private fun fetchMoviesByQuery(keyword: String) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getMoviesByKeywordUseCase(keyword = keyword) },
            onSuccess = ::onFetchMoviesSuccess,
            onError = ::onFetchError,
        )
    }

    private fun onFetchMoviesSuccess(movies: List<Movie>) {
        Log.e("bk", movies.toString())

        updateState {
            it.copy(
                movies = movies.toMoveUiStates(),
                isLoading = false,
                errorUiState = null
            )
        }
        Log.e("bk", "ui movies: ${state.value.movies}")
    }

    private fun fetchTvShowsByQuery(keyword: String) {
        tryToExecute(
            action = { getTvShowByKeywordUseCase(keyword = keyword) },
            onSuccess = ::onFetchTvShowsSuccess,
            onError = ::onFetchError,
        )
    }

    private fun onFetchTvShowsSuccess(tvShows: List<TvShow>) {
        updateState {
            it.copy(
                tvShows = tvShows.toTvShowUiStates(),
                isLoading = false,
                errorUiState = null
            )
        }
    }

    override fun onTextValuedChanged(text: String) {
        queryFlow.value = text
        if (text.isBlank()) {
            updateState { it.copy(query = text, movies = emptyList(), tvShows = emptyList()) }
        } else {
            updateState { it.copy(query = text) }
        }
    }

    override fun onSearchActionClicked() {
        tryToExecute(
            action = { addRecentSearchUseCase(state.value.query) },
            onSuccess = { loadRecentSearches() },
            onError = ::onFetchError,
        )
    }

    override fun onFilterButtonClicked() = updateState { it.copy(isDialogVisible = true) }

    override fun onNavigateBackClicked() = sendNewEffect(SearchUiEffect.NavigateBack)

    override fun onWorldSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToWorldSearch)

    override fun onActorSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToActorSearch)

    override fun onMovieCardClicked() = sendNewEffect(SearchUiEffect.NavigateToMovieDetails)

    override fun onTabOptionClicked(tabOption: TabOption) {
        observeQueryFlow()
        updateState {
            it.copy(
                selectedTabOption = tabOption,
                movies = state.value.movies,
                tvShows = state.value.tvShows,
            )
        }
    }

    override fun onRecentSearchClicked(keyword: String) = onTextValuedChanged(keyword)

    override fun onClearRecentSearch(keyword: String) {
        updateState { it.copy(isLoading = true) }

        tryToExecute(
            action = { clearRecentSearchUseCase(searchKeyword = keyword) },
            onSuccess = { loadRecentSearches() },
            onError = ::onFetchError,
        )
    }

    override fun onClearAllRecentSearches() {
        updateState { it.copy(isLoading = true) }

        tryToExecute(
            action = { clearAllRecentSearchesUseCase() },
            onSuccess = ::onClearAllRecentSearchesSuccess,
            onError = ::onFetchError,
        )
    }

    private fun onClearAllRecentSearchesSuccess(unit: Unit) {
        updateState {
            it.copy(
                recentSearches = emptyList(),
                isLoading = false
            )
        }
    }

    override fun onCancelButtonClicked() {
        updateState {
            it.copy(
                isDialogVisible = false,
                filterItemUiState = it.filterItemUiState.copy(
                    isLoading = false,
                )
            )
        }
    }

    override fun onRatingStarChanged(ratingIndex: Int) {
        updateState {
            it.copy(
                filterItemUiState = it.filterItemUiState.copy(selectedStarIndex = ratingIndex)
            )
        }
    }

    override fun onGenreButtonChanged(genreType: GenreType) {
        Log.e("bk", genreType.name)
        updateState {
            it.copy(
                filterItemUiState = state.value.filterItemUiState.copy(
                    genreItemUiStates = it.filterItemUiState.genreItemUiStates.selectByType(
                        genreType
                    )
                )
            )
        }
    }

    override fun onApplyButtonClicked() {
        updateState {
            it.copy(
                filterItemUiState = it.filterItemUiState.copy(
                    isLoading = true,
                )
            )
        }

        when (state.value.selectedTabOption) {
            TabOption.MOVIES -> applyMoviesFilter()
            TabOption.TV_SHOWS -> applyTvShowsFilter()
        }
    }

    private fun applyMoviesFilter() {
        val currentGenreItemUiStates = state.value.filterItemUiState.genreItemUiStates
        tryToExecute(
            action = {
                getMoviesByKeywordUseCase(
                    keyword = state.value.query,
                    rating = state.value.filterItemUiState.selectedStarIndex.toFloat(),
                    categoryName = currentGenreItemUiStates.getSelectedOne(genres = currentGenreItemUiStates).type.name
                )
            },
            onSuccess = ::onMoviesFilteredSuccess,
            onError = ::onFilterError,
        )
    }

    private fun onMoviesFilteredSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                movies = movies.toMoveUiStates(), isLoading = false, isDialogVisible = false,
                filterItemUiState = it.filterItemUiState.copy(
                    isLoading = false,
                )
            )
        }
    }

    private fun applyTvShowsFilter() {
        val currentGenreItemUiStates = state.value.filterItemUiState.genreItemUiStates
        tryToExecute(
            action = {
                getTvShowByKeywordUseCase(
                    keyword = state.value.query,
                    rating = state.value.filterItemUiState.selectedStarIndex.toFloat(),
                    categoryName = currentGenreItemUiStates.getSelectedOne(currentGenreItemUiStates).type.name
                )
            },
            onSuccess = ::onTvShowsFilteredSuccess,
            onError = ::onFilterError,
        )
    }

    private fun onTvShowsFilteredSuccess(tvShows: List<TvShow>) {
        updateState {
            it.copy(
                tvShows = tvShows.toTvShowUiStates(),
                isLoading = false,
                isDialogVisible = false,
                filterItemUiState = it.filterItemUiState.copy(
                    isLoading = false,
                )
            )
        }
    }

    private fun onFilterError(exception: AflamiException) {
        onFetchError(exception)
        updateState {
            it.copy(
                isDialogVisible = false,
                filterItemUiState = it.filterItemUiState.copy(
                    isLoading = false
                )
            )
        }
    }

    private fun onFetchError(exception: AflamiException) {
        Log.e("bk", "exception: $exception")

        updateState { it.copy(errorUiState = mapToSearchUiState(exception), isLoading = false) }
    }

    override fun onClearButtonClicked() {
        updateState { it.copy(filterItemUiState = FilterItemUiState()) }
    }
}