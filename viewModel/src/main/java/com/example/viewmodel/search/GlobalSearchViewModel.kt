package com.example.viewmodel.search

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
import com.example.viewmodel.common.categories.MovieCategoryItemUiState.Companion.getSelectedOne
import com.example.viewmodel.common.categories.MovieCategoryItemUiState.Companion.selectByType
import com.example.viewmodel.common.categories.MovieCategoryType
import com.example.viewmodel.common.categories.TvShowCategoryItemUiState.Companion.getSelectedOne
import com.example.viewmodel.common.categories.TvShowCategoryItemUiState.Companion.selectByType
import com.example.viewmodel.common.categories.TvShowCategoryType
import com.example.viewmodel.common.categories.toMovieGenreType
import com.example.viewmodel.common.categories.toTvShowGenre
import com.example.viewmodel.common.toMoveUiStates
import com.example.viewmodel.common.toTvShowUiStates
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
        rating: Float = 0f,
        movieCategoryType: MovieCategoryType = MovieCategoryType.ALL
    ) {
        tryToExecute(
            action = {
                getMoviesByKeywordUseCase(
                    keyword = keyword,
                    rating = rating,
                    movieGenre = movieCategoryType.toMovieGenreType()
                )
            },
            onSuccess = ::onFetchMoviesSuccess,
            onError = ::onFetchError,
            onCompletion = ::stopLoading
        )
    }

    private fun onFetchMoviesSuccess(movies: List<Movie>) {
        updateState { it.copy(movies = movies.toMoveUiStates()) }
    }

    private fun fetchTvShowsByQuery(
        keyword: String,
        rating: Float = 0f,
        movieCategoryType: TvShowCategoryType = TvShowCategoryType.ALL
    ) {
        tryToExecute(
            action = {
                getTvShowByKeywordUseCase(
                    keyword = keyword,
                    rating = rating,
                    tvShowGenre = movieCategoryType.toTvShowGenre()
                )
            },
            onSuccess = ::onFetchTvShowsSuccess,
            onError = ::onFetchError,
            onCompletion = ::stopLoading
        )
    }

    private fun onFetchTvShowsSuccess(tvShows: List<TvShow>) {
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

    override fun onFilterButtonClicked() = updateState { it.copy(isDialogVisible = true) }

    override fun onNavigateBackClicked() {
        if (state.value.query.isNotEmpty()) {
            onClearSearch()
        } else {
            sendNewEffect(SearchUiEffect.NavigateBack)
        }
    }

    override fun onWorldSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToWorldSearch)

    override fun onActorSearchCardClicked() = sendNewEffect(SearchUiEffect.NavigateToActorSearch)

    override fun onMovieCardClicked() = sendNewEffect(SearchUiEffect.NavigateToMovieDetails)

    override fun onTabOptionClicked(tabOption: TabOption) {
        observeSearchQueryChanges()
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
            onCompletion = ::stopLoading
        )
    }

    override fun onClearSearch() {
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
    }

    override fun onCancelButtonClicked() {
        updateState { it.copy(isDialogVisible = false, isLoading = false) }
    }

    override fun onRatingStarChanged(ratingIndex: Int) {
        updateState { it.copy(filterItemUiState = it.filterItemUiState.copy(selectedStarIndex = ratingIndex)) }
    }

    override fun onMovieGenreButtonChanged(movieCategoryType: MovieCategoryType) {
        updateState {
            it.copy(
                filterItemUiState = state.value.filterItemUiState.copy(
                    movieGenreItemUiStates = it.filterItemUiState.movieGenreItemUiStates.selectByType(
                        movieCategoryType
                    )
                )
            )
        }
    }

    override fun onTvGenreButtonChanged(tvShowCategoryType: TvShowCategoryType) {
        updateState {
            it.copy(
                filterItemUiState = state.value.filterItemUiState.copy(
                    tvShowGenreItemUiStates = it.filterItemUiState.tvShowGenreItemUiStates.selectByType(
                        tvShowCategoryType
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
        val currentCategoryItemUiStates = state.value.filterItemUiState.movieGenreItemUiStates
        tryToExecute(
            action = {
                getMoviesByKeywordUseCase(
                    keyword = state.value.query,
                    rating = state.value.filterItemUiState.selectedStarIndex.toFloat(),
                    movieGenre = currentCategoryItemUiStates.getSelectedOne(categories = currentCategoryItemUiStates).type.toMovieGenreType()
                )
            },
            onSuccess = ::onMoviesFilteredSuccess,
            onError = ::onFetchError,
            onCompletion = ::resetFilterState
        )
    }

    private fun onMoviesFilteredSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                movies = movies.toMoveUiStates(),
                isLoading = false
            )
        }
    }

    private fun applyTvShowsFilter() {
        val currentGenreItemUiStates = state.value.filterItemUiState.tvShowGenreItemUiStates
        tryToExecute(
            action = {
                getTvShowByKeywordUseCase(
                    keyword = state.value.query,
                    rating = state.value.filterItemUiState.selectedStarIndex.toFloat(),
                    tvShowGenre = currentGenreItemUiStates.getSelectedOne(currentGenreItemUiStates).type.toTvShowGenre()
                )
            },
            onSuccess = ::onTvShowsFilteredSuccess,
            onError = ::onFetchError,
            onCompletion = ::resetFilterState
        )
    }

    private fun onTvShowsFilteredSuccess(tvShows: List<TvShow>) {
        updateState {
            it.copy(
                tvShows = tvShows.toTvShowUiStates(),
                isLoading = false
            )
        }
    }


    private fun onFetchError(exception: AflamiException) {
        updateState { it.copy(errorUiState = mapToSearchUiState(exception)) }
    }

    override fun onClearButtonClicked() = resetFilterState()

    private fun resetFilterState() =
        updateState { it.copy(filterItemUiState = FilterItemUiState()) }

    private fun stopLoading() = updateState { it.copy(isLoading = false) }
}
