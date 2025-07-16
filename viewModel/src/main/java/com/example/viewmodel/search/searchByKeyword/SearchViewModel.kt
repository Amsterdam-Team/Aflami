package com.example.viewmodel.search.searchByKeyword

import android.R.attr.rating
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.exceptions.AflamiException
import com.example.domain.useCase.GetAndFilterMoviesByKeywordUseCase
import com.example.domain.useCase.GetAndFilterTvShowsByKeywordUseCase
import com.example.domain.useCase.search.AddRecentSearchUseCase
import com.example.domain.useCase.search.ClearAllRecentSearchesUseCase
import com.example.domain.useCase.search.ClearRecentSearchUseCase
import com.example.domain.useCase.search.GetRecentSearchesUseCase
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.paging.PagingSource
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.common.MediaItemUiState
import com.example.viewmodel.common.toMediaItemUiState
import com.example.viewmodel.search.mapper.getSelectedGenreType
import com.example.viewmodel.search.mapper.selectByMovieGenre
import com.example.viewmodel.search.mapper.selectByTvGenre
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
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
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<SearchUiState, SearchUiEffect>(SearchUiState(), dispatcherProvider),
    SearchInteractionListener,
    FilterInteractionListener {
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
            onCompletion = ::onCompletion,
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
            _keyword
                .map(String::trim)
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
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getAndFilterMoviesByKeywordUseCase(
                                keyword = keyword,
                                page = page,
                            )
                        }
                    },
                ).flow
                    .map { it.map { movie -> movie.toMediaItemUiState() } }
                    .cachedIn(viewModelScope)
            },
            onSuccess = ::onFetchMoviesSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCompletion,
        )
    }

    private fun onFetchMoviesSuccess(movies: Flow<PagingData<MediaItemUiState>>) {
        applyMoviesFilter()
        updateState { it.copy(movies = movies, errorUiState = null) }
    }

    private fun fetchTvShowsByKeyword(keyword: String) {
        startLoading()
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getAndFilterTvShowsByKeywordUseCase(
                                keyword = keyword,
                                page = page,
                                rating = rating,
                            )
                        }
                    },
                ).flow.map { it.map { tvShow -> tvShow.toMediaItemUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = ::onFetchTvShowsSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCompletion,
        )
    }

    private fun onFetchTvShowsSuccess(tvShows: Flow<PagingData<MediaItemUiState>>) {
        applyTvShowsFilter()
        updateState { it.copy(tvShows = tvShows, errorUiState = null) }
    }

    private fun applyMoviesFilter() {
        val currentCategoryItemUiStates = state.value.filterItemUiState.selectableMovieGenres
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getAndFilterMoviesByKeywordUseCase(
                                keyword = state.value.keyword,
                                page = page,
                                rating = state.value.filterItemUiState.selectedStarIndex,
                                movieGenre = currentCategoryItemUiStates.getSelectedGenreType(),
                            )
                        }
                    },
                ).flow.map { it.map { movie -> movie.toMediaItemUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = ::onMoviesFilteredSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCancelButtonClicked,
        )
    }

    private fun applyTvShowsFilter() {
        val currentGenreItemUiStates = state.value.filterItemUiState.selectableTvShowGenres
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getAndFilterTvShowsByKeywordUseCase(
                                keyword = state.value.keyword,
                                page = page,
                                rating = state.value.filterItemUiState.selectedStarIndex,
                                tvGenre = currentGenreItemUiStates.getSelectedGenreType(),
                            )
                        }
                    },
                ).flow.map { it.map { tvShow -> tvShow.toMediaItemUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = ::onTvShowsFilteredSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCancelButtonClicked,
        )
    }

    private fun onFetchError(exception: AflamiException) {
        updateState { it.copy(errorUiState = mapToSearchUiState(exception)) }
    }

    private fun resetFilterState() = updateState { it.copy(filterItemUiState = FilterItemUiState()) }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }

    private fun startLoading() {
        updateState {
            it.copy(
                isLoading = true,
            )
        }
    }

    override fun onSearchActionClicked() {
        onKeywordValuedChanged(state.value.keyword)
        tryToExecute(
            action = { addRecentSearchUseCase(state.value.keyword) },
            onSuccess = { loadRecentSearches() },
            onError = ::onFetchError,
            onCompletion = ::onCompletion,
        )
    }

    override fun onNavigateBackClicked() {
        if (state.value.keyword.isNotEmpty()) {
            onSearchCleared()
        } else {
            sendNewEffect(SearchUiEffect.NavigateBack)
        }
    }

    override fun onKeywordValuedChanged(keyword: String) {
        _keyword.update { oldText -> keyword }
        updateState { it.copy(keyword = keyword) }
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
                isLoading = true, // TODO: Check
                filterItemUiState = FilterItemUiState(),
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
            onCompletion = ::onCompletion,
        )
    }

    override fun onSearchCleared() {
        updateState { currentState ->
            currentState.copy(
                keyword = "",
                isDialogVisible = false,
                filterItemUiState = FilterItemUiState(),
            )
        }
    }

    override fun onFilterButtonClicked() {
        updateState { it.copy(isDialogVisible = true, isLoading = false) }
    }

    override fun onCancelButtonClicked() {
        updateState {
            it.copy(
                isDialogVisible = false,
                filterItemUiState = it.filterItemUiState.copy(isLoading = false),
            )
        }
    }

    override fun onRatingStarChanged(ratingIndex: Int) {
        updateState { it.copy(filterItemUiState = it.filterItemUiState.copy(selectedStarIndex = ratingIndex)) }
    }

    override fun onMovieGenreButtonChanged(genreType: MovieGenre) {
        updateState {
            it.copy(
                filterItemUiState =
                    state.value.filterItemUiState.copy(
                        selectableMovieGenres =
                            it.filterItemUiState.selectableMovieGenres.selectByMovieGenre(
                                genreType,
                            ),
                    ),
            )
        }
    }

    override fun onTvGenreButtonChanged(genreType: TvShowGenre) {
        updateState {
            it.copy(
                filterItemUiState =
                    state.value.filterItemUiState.copy(
                        selectableTvShowGenres =
                            it.filterItemUiState.selectableTvShowGenres.selectByTvGenre(
                                genreType,
                            ),
                    ),
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

    private fun onMoviesFilteredSuccess(movies: Flow<PagingData<MediaItemUiState>>) {
        updateState {
            it.copy(
                movies = movies,
                errorUiState = null,
                filterItemUiState = it.filterItemUiState.copy(isLoading = false),
            )
        }
    }

    private fun onTvShowsFilteredSuccess(tvShows: Flow<PagingData<MediaItemUiState>>) {
        updateState {
            it.copy(
                tvShows = tvShows,
                filterItemUiState = it.filterItemUiState.copy(isLoading = false),
                errorUiState = null,
            )
        }
    }

    override fun onClearButtonClicked() = resetFilterState()
}
