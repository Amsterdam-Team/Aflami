package com.example.viewmodel.search.searchByKeyword

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.useCase.GetAndFilterMoviesByKeywordUseCase
import com.example.domain.useCase.GetAndFilterTvShowsByKeywordUseCase
import com.example.domain.useCase.search.AddRecentSearchUseCase
import com.example.domain.useCase.search.ClearAllRecentSearchesUseCase
import com.example.domain.useCase.search.ClearRecentSearchUseCase
import com.example.domain.useCase.search.GetRecentSearchesUseCase
import com.example.entity.Movie
import com.example.entity.TvShow
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.common.toMoveUiStates
import com.example.viewmodel.common.toTvShowUiStates
import com.example.viewmodel.search.mapper.getSelectedGenreType
import com.example.viewmodel.search.mapper.selectByMovieGenre
import com.example.viewmodel.search.mapper.selectByTvGenre
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@OptIn(FlowPreview::class)
class SearchViewModel(
    private val getAndFilterMoviesByKeywordUseCase: GetAndFilterMoviesByKeywordUseCase,
    private val getAndFilterTvShowsByKeywordUseCase: GetAndFilterTvShowsByKeywordUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val addRecentSearchUseCase: AddRecentSearchUseCase,
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase,
    private val clearAllRecentSearchesUseCase: ClearAllRecentSearchesUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SearchUiState, SearchUiEffect>(SearchUiState(), dispatcherProvider),
    SearchInteractionListener, FilterInteractionListener {

    private val _keyword = MutableStateFlow(state.value.keyword)

    init {
        loadRecentSearches()
        observeSearchKeywordChanges()
    }

    private fun loadRecentSearches() {
        startLoading()
        tryToExecute(
            action = { getRecentSearchesUseCase() },
            onSuccess = ::onLoadRecentSearchesSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCompletion
        )
    }

    private fun onLoadRecentSearchesSuccess(recentSearches: List<String>) {
        updateState { it.copy(recentSearches = recentSearches, errorUiState = null) }
    }

    private fun onClearAllRecentSearchesSuccess(unit: Unit) {
        updateState { it.copy(recentSearches = emptyList()) }
        return unit
    }


    private fun observeSearchKeywordChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            _keyword.map(String::trim)
                .debounce(300)
                .filter(String::isNotBlank)
                .collectLatest(::onSearchKeywordChanged)
        }
    }

    private fun onSearchKeywordChanged(keyword: String) {
        when (state.value.selectedTabOption) {
            TabOption.MOVIES -> fetchMoviesByKeyword(keyword)
            TabOption.TV_SHOWS -> fetchTvShowsByKeyword(keyword)
        }
    }

    private fun fetchMoviesByKeyword(keyword: String) {
        startLoading()
        tryToExecute(
            action = { getAndFilterMoviesByKeywordUseCase(keyword = keyword) },
            onSuccess = ::onFetchMoviesSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCompletion
        )
    }

    private fun onFetchMoviesSuccess(movies: List<Movie>) {
        updateState { it.copy(movies = movies.toMoveUiStates(), errorUiState = null) }
    }

    private fun fetchTvShowsByKeyword(keyword: String) {
        startLoading()
        tryToExecute(
            action = {
                getAndFilterTvShowsByKeywordUseCase(keyword = keyword)
            },
            onSuccess = ::onFetchTvShowsSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCompletion
        )
    }

    private fun onFetchTvShowsSuccess(tvShows: List<TvShow>) {
        updateState { it.copy(tvShows = tvShows.toTvShowUiStates(), errorUiState = null) }
    }

    private fun applyMoviesFilter() {
        val currentCategoryItemUiStates = state.value.filterItemUiState.selectableMovieGenres
        tryToExecute(
            action = {
                getAndFilterMoviesByKeywordUseCase(
                    keyword = state.value.keyword,
                    rating = state.value.filterItemUiState.selectedStarIndex,
                    movieGenre = currentCategoryItemUiStates.getSelectedGenreType()
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
                filterItemUiState = it.filterItemUiState.copy(isLoading = false),
                errorUiState = null
            )
        }
    }

    private fun applyTvShowsFilter() {
        val currentGenreItemUiStates = state.value.filterItemUiState.selectableTvShowGenres
        tryToExecute(
            action = {
                getAndFilterTvShowsByKeywordUseCase(
                    keyword = state.value.keyword,
                    rating = state.value.filterItemUiState.selectedStarIndex,
                    tvGenre = currentGenreItemUiStates.getSelectedGenreType()
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

    private fun resetFilterState() =
        updateState { it.copy(filterItemUiState = FilterItemUiState()) }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }

    private fun startLoading() {
        updateState {
            it.copy(
                isLoading = true,
            )
        }
    }

    override fun onKeywordValuedChanged(keyword: String) {
        _keyword.update { oldText -> keyword }
        updateState { it.copy(keyword = keyword) }
    }

    override fun onSearchActionClicked() {
        onKeywordValuedChanged(state.value.keyword)
        tryToExecute(
            action = { addRecentSearchUseCase(state.value.keyword) },
            onSuccess = { loadRecentSearches() },
            onError = ::onFetchError,
            onCompletion = ::onCompletion
        )
    }

    override fun onNavigateBackClicked() {
        if (state.value.keyword.isNotEmpty()) {
            onSearchCleared()
        } else {
            sendNewEffect(SearchUiEffect.NavigateBack)
        }
    }

    override fun onWorldSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToWorldSearch)

    override fun onActorSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToActorSearch)

    override fun onRetryQuestClicked() {
        observeSearchKeywordChanges()
        updateState { it.copy(isLoading = true, errorUiState = null) }
    }

    override fun onMovieCardClicked() = sendNewEffect(SearchUiEffect.NavigateToMovieDetails)

    override fun onTabOptionClicked(tabOption: TabOption) {
        observeSearchKeywordChanges()
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
        onKeywordValuedChanged(keyword)
        observeSearchKeywordChanges()
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
            onCompletion = ::onCompletion
        )
    }

    override fun onSearchCleared() {
        updateState { currentState ->
            currentState.copy(
                keyword = "",
                isDialogVisible = false,
                filterItemUiState = FilterItemUiState()
            )
        }
    }

    override fun onMovieClicked(movieId: Long) {
        updateState { it.copy(selectedMovieId = movieId) }
        sendNewEffect(SearchUiEffect.NavigateToMovieDetails)
    }

    override fun onFilterButtonClicked() {
        updateState { it.copy(isDialogVisible = true, isLoading = false) }
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

    override fun onCancelButtonClicked() {
        updateState {
            it.copy(
                isDialogVisible = false,
                filterItemUiState = it.filterItemUiState.copy(isLoading = false)
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

    override fun onClearButtonClicked() = resetFilterState()
}