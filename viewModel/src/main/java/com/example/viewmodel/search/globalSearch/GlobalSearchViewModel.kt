package com.example.viewmodel.search.globalSearch

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
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.common.toMoveUiStates
import com.example.viewmodel.common.toTvShowUiStates
import com.example.viewmodel.search.globalSearch.genre.MovieGenre
import com.example.viewmodel.search.globalSearch.genre.TvShowGenre
import com.example.viewmodel.search.mapper.getSelectedGenreType
import com.example.viewmodel.search.mapper.mapToGenreId
import com.example.viewmodel.search.mapper.selectByMovieGenre
import com.example.viewmodel.search.mapper.selectByTvGenre
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
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
            onCompletion = ::stopLoading
        )
    }

    private fun onLoadRecentSearchesSuccess(recentSearches: List<String>) {
        updateState { it.copy(recentSearches = recentSearches, isLoading = false) }
    }

    private fun observeSearchQueryChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            _query.debounce(300)
                .map(String::trim)
                .filter(String::isNotBlank)
                .collect(::onSearchQueryChanged)
        }
    }

    private fun onSearchQueryChanged(trimmedQuery: String) {
        updateState {
            it.copy(
                isLoading = true,
                errorUiState = null,
                movies = emptyList(),
                tvShows = emptyList()
            )
        }
        when (state.value.selectedTabOption) {
            TabOption.MOVIES -> fetchMoviesByQuery(trimmedQuery)
            TabOption.TV_SHOWS -> fetchTvShowsByQuery(trimmedQuery)
        }
    }

    private fun fetchMoviesByQuery(
        keyword: String,
        rating: Int = 0,
        movieGenre: MovieGenre = MovieGenre.ALL
    ) {
        tryToExecute(
            action = {
                getMoviesByKeywordUseCase(
                    keyword = keyword,
                    rating = rating,
                    movieGenreId = movieGenre.mapToGenreId()
                )
            },
            onSuccess = ::onFetchMoviesSuccess,
            onError = ::onFetchError,
            onCompletion = ::stopLoading
        )
    }

    private fun onFetchMoviesSuccess(movies: List<Movie>) {
        applyMoviesFilter()
        updateState { it.copy(movies = movies.toMoveUiStates()) }
    }

    private fun fetchTvShowsByQuery(
        keyword: String,
        rating: Float = 0f,
        tvGenre: TvShowGenre = TvShowGenre.ALL
    ) {
        tryToExecute(
            action = {
                getTvShowByKeywordUseCase(
                    keyword = keyword,
                    rating = rating,
                    tvShowGenreId = tvGenre.mapToGenreId()
                )
            },
            onSuccess = ::onFetchTvShowsSuccess,
            onError = ::onFetchError,
            onCompletion = ::stopLoading
        )
    }

    private fun onFetchTvShowsSuccess(tvShows: List<TvShow>) {
        applyTvShowsFilter()
        updateState { it.copy(tvShows = tvShows.toTvShowUiStates()) }
    }

    override fun onTextValuedChanged(text: String) {
        _query.update { oldText -> text }
        updateState { it.copy(query = text) }
    }

    override fun onSearchActionClicked() {
        tryToExecute(
            action = { addRecentSearchUseCase(state.value.query) },
            onSuccess = { loadRecentSearches() },
            onError = ::onFetchError,
        )
    }

    override fun onFilterButtonClicked() =
        updateState { it.copy(isDialogVisible = true, isLoading = false) }

    override fun onNavigateBackClicked() {
        if (state.value.query.isNotEmpty()) {
            onSearchCleared()
        } else {
            sendNewEffect(SearchUiEffect.NavigateBack)
        }
    }

    override fun onWorldSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToWorldSearch)

    override fun onActorSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToActorSearch)

    override fun onRetryQuestClicked() {
        updateState { it.copy(isLoading = true, errorUiState = null) }
        observeSearchQueryChanges()
    }

    override fun onMovieCardClicked() = sendNewEffect(SearchUiEffect.NavigateToMovieDetails)

    override fun onTabOptionClicked(tabOption: TabOption) {
        observeSearchQueryChanges()
        updateState {
            it.copy(
                selectedTabOption = tabOption,
                movies = state.value.movies,
                tvShows = state.value.tvShows,
                isLoading = true,
                filterItemUiState = FilterItemUiState()
            )
        }
    }

    override fun onRecentSearchClicked(keyword: String) {
        onTextValuedChanged(keyword)
        observeSearchQueryChanges()
    }

    override fun onRecentSearchCleared(keyword: String) {
        updateState { it.copy(isLoading = true) }

        tryToExecute(
            action = { clearRecentSearchUseCase(searchKeyword = keyword) },
            onSuccess = { loadRecentSearches() },
            onError = ::onFetchError,
        )
    }

    override fun onAllRecentSearchesCleared() {
        updateState { it.copy(isLoading = true) }

        tryToExecute(
            action = { clearAllRecentSearchesUseCase() },
            onSuccess = ::onClearAllRecentSearchesSuccess,
            onError = ::onFetchError,
            onCompletion = ::stopLoading
        )
    }

    override fun onSearchCleared() {
        updateState { currentState ->
            currentState.copy(
                query = "",
                movies = emptyList(),
                tvShows = emptyList(),
                selectedTabOption = TabOption.MOVIES,
                errorUiState = null,
                isDialogVisible = false,
                filterItemUiState = FilterItemUiState()
            )
        }
    }

    private fun onClearAllRecentSearchesSuccess(unit: Unit) {
        updateState { it.copy(recentSearches = emptyList()) }
        return unit
    }

    override fun onCancelButtonClicked() {
        updateState {
            it.copy(
                isDialogVisible = false,
                filterItemUiState = it.filterItemUiState.copy(isLoading = false)
            )
        }
    }

    override fun onRatingStarChanged(ratingIndex: Int) {
        updateState { it.copy(filterItemUiState = it.filterItemUiState.copy(selectedStarIndex = ratingIndex)) }
    }

    override fun onMovieGenreButtonChanged(genreType: MovieGenre) {
        updateState {
            it.copy(
                filterItemUiState = state.value.filterItemUiState.copy(
                    selectableMovieGenres = it.filterItemUiState.selectableMovieGenres.selectByMovieGenre(
                        genreType
                    )
                )
            )
        }
    }

    override fun onTvGenreButtonChanged(genreType: TvShowGenre) {
        updateState {
            it.copy(
                filterItemUiState = state.value.filterItemUiState.copy(
                    selectableTvShowGenres = it.filterItemUiState.selectableTvShowGenres.selectByTvGenre(
                        genreType
                    )
                )
            )
        }
    }

    override fun onApplyButtonClicked() {
        updateState {
            it.copy(
                filterItemUiState = it.filterItemUiState.copy(isLoading = true),
                isDialogVisible = false,
            )
        }

        when (state.value.selectedTabOption) {
            TabOption.MOVIES -> applyMoviesFilter()
            TabOption.TV_SHOWS -> applyTvShowsFilter()
        }
    }

    private fun applyMoviesFilter() {
        val currentCategoryItemUiStates = state.value.filterItemUiState.selectableMovieGenres
        tryToExecute(
            action = {
                getMoviesByKeywordUseCase(
                    keyword = state.value.query,
                    rating = state.value.filterItemUiState.selectedStarIndex,
                    movieGenreId = currentCategoryItemUiStates.getSelectedGenreType().mapToGenreId()
                )
            },
            onSuccess = ::onMoviesFilteredSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCancelButtonClicked
        )
    }

    private fun onMoviesFilteredSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                movies = movies.toMoveUiStates(),
                errorUiState = null,
                filterItemUiState = it.filterItemUiState.copy(isLoading = false)
            )
        }
    }

    private fun applyTvShowsFilter() {
        val currentGenreItemUiStates = state.value.filterItemUiState.selectableTvShowGenres
        tryToExecute(
            action = {
                getTvShowByKeywordUseCase(
                    keyword = state.value.query,
                    rating = state.value.filterItemUiState.selectedStarIndex.toFloat(),
                    tvShowGenreId = currentGenreItemUiStates.getSelectedGenreType().mapToGenreId()
                )
            },
            onSuccess = ::onTvShowsFilteredSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCancelButtonClicked
        )
    }

    private fun onTvShowsFilteredSuccess(tvShows: List<TvShow>) {
        updateState {
            it.copy(
                tvShows = tvShows.toTvShowUiStates(),
                filterItemUiState = it.filterItemUiState.copy(isLoading = false),
                errorUiState = null
            )
        }
    }


    private fun onFetchError(exception: AflamiException) {
        updateState { it.copy(errorUiState = mapToSearchUiState(exception)) }
    }

    override fun onClearButtonClicked() = resetFilterState()

    private fun resetFilterState() = updateState { it.copy(filterItemUiState = FilterItemUiState()) }

    private fun stopLoading() = updateState { it.copy(isLoading = false) }
}
