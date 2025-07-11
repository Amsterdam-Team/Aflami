package com.example.viewmodel.search

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.useCase.GetMoviesByKeywordUseCase
import com.example.domain.useCase.GetTvShowByKeywordUseCase
import com.example.domain.useCase.search.ClearAllRecentSearchesUseCase
import com.example.domain.useCase.search.ClearRecentSearchUseCase
import com.example.domain.useCase.search.GetRecentSearchesUseCase
import com.example.entity.Movie
import com.example.entity.TvShow
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.common.GenreType
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.common.toMoveUiStates
import com.example.viewmodel.common.toTvShowUiStates
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class GlobalSearchViewModel(
    private val getMoviesByKeywordUseCase: GetMoviesByKeywordUseCase,
    private val getTvShowByKeywordUseCase: GetTvShowByKeywordUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase,
    private val clearAllRecentSearchesUseCase: ClearAllRecentSearchesUseCase,
) : BaseViewModel<SearchUiState, SearchUiEffect>(SearchUiState()),
    GlobalSearchInteractionListener, FilterInteractionListener {

    private val _query = MutableStateFlow(state.value.query)

    init {
        loadRecentSearches()
        observeSearchQueryChanges()
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

    private fun observeSearchQueryChanges() {
        viewModelScope.launch {
            _query.debounce(300)
                .map(String::trim)
                .collect(::onSearchQueryChanged)
        }
    }

    private fun onSearchQueryChanged(trimmedQuery: String) {
        when (state.value.selectedTabOption) {
            TabOption.MOVIES -> fetchMoviesByQuery(trimmedQuery)
            TabOption.TV_SHOWS -> fetchTvShowsByQuery(trimmedQuery)
        }
    }

    private fun fetchMoviesByQuery(keyword: String) {
        tryToExecute(
            action = { getMoviesByKeywordUseCase(keyword = keyword) },
            onSuccess = ::onFetchMoviesSuccess,
            onError = ::onFetchError,
        )
    }

    private fun onFetchMoviesSuccess(movies: List<Movie>) {
        updateState { it.copy(movies = movies.toMoveUiStates()) }
    }

    private fun fetchTvShowsByQuery(keyword: String) {
        tryToExecute(
            action = { getTvShowByKeywordUseCase(keyword = keyword) },
            onSuccess = ::onFetchTvShowsSuccess,
            onError = ::onFetchError,
        )
    }

    private fun onFetchTvShowsSuccess(tvShows: List<TvShow>) {
        updateState { it.copy(tvShows = tvShows.toTvShowUiStates()) }
    }

    override fun onTextValuedChanged(text: String) {
        _query.value = text
        updateState { it.copy(query = text) }
    }

    override fun onFilterButtonClicked() = updateState { it.copy(isDialogVisible = true) }

    override fun onNavigateBackClicked() = sendNewEffect(SearchUiEffect.NavigateBack)

    override fun onWorldSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToWorldSearch)

    override fun onActorSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToActorSearch)

    override fun onMovieCardClicked() = sendNewEffect(SearchUiEffect.NavigateToMovieDetails)

    override fun onTabOptionClicked(tabOption: TabOption) {
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
                filterItemUiState = FilterItemUiState()
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
        updateState {
            it.copy(
                filterItemUiState = it.filterItemUiState.copy(
                    selectedGenre = it.filterItemUiState.selectedGenre.copy(
                        type = genreType,
                        isSelected = true
                    )
                )
            )
        }
    }

    override fun onApplyButtonClicked() {
        updateState { it.copy(isLoading = true) }

        when (state.value.selectedTabOption) {
            TabOption.MOVIES -> applyMoviesFilter()
            TabOption.TV_SHOWS -> applyTvShowsFilter()
        }
    }

    private fun applyMoviesFilter() {
        tryToExecute(
            action = {
                getMoviesByKeywordUseCase(
                    keyword = state.value.query,
                    rating = state.value.filterItemUiState.selectedStarIndex.toFloat(),  // TODO(format enum names)
                    categoryName = state.value.filterItemUiState.selectedGenre.type.name
                )
            },
            onSuccess = ::onMoviesFilteredSuccess,
            onError = ::onFilterError,
        )
    }

    private fun onMoviesFilteredSuccess(movies: List<Movie>) {
        updateState {
            it.copy(movies = movies.toMoveUiStates(), isLoading = false, isDialogVisible = false)
        }
    }

    private fun applyTvShowsFilter() {
        tryToExecute(
            action = {
                getTvShowByKeywordUseCase(
                    keyword = state.value.query,
                    rating = state.value.filterItemUiState.selectedStarIndex.toFloat(),  // TODO(format enum names)
                    categoryName = state.value.filterItemUiState.selectedGenre.type.name
                )
            },
            onSuccess = ::onTvShowsFilteredSuccess,
            onError = ::onFilterError,
        )
    }

    private fun onTvShowsFilteredSuccess(tvShows: List<TvShow>) {
        updateState {
            it.copy(tvShows = tvShows.toTvShowUiStates(), isLoading = false, isDialogVisible = false)
        }
    }

    private fun onFilterError(exception: AflamiException) {
        onFetchError(exception)
        updateState { it.copy(isDialogVisible = false) }
    }

    private fun onFetchError(exception: AflamiException) {
        updateState { it.copy(errorUiState = mapToSearchUiState(exception), isLoading = false) }
    }

    override fun onClearButtonClicked() {
        updateState { it.copy(filterItemUiState = FilterItemUiState()) }
    }
}