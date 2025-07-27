package com.amsterdam.viewmodel.search.keywordSearch

import android.R.attr.rating
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.search.GetAndFilterMoviesByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterTvShowsByKeywordUseCase
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.search.mapper.getSelectedGenreType
import com.amsterdam.viewmodel.search.mapper.selectByMovieGenre
import com.amsterdam.viewmodel.search.mapper.selectByTvGenre
import com.amsterdam.viewmodel.search.mapper.toMediaItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState
import com.amsterdam.viewmodel.utils.debounceSearch
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getAndFilterMoviesByKeywordUseCase: GetAndFilterMoviesByKeywordUseCase,
    private val getAndFilterTvShowsByKeywordUseCase: GetAndFilterTvShowsByKeywordUseCase,
    private val recentSearchesUseCase: RecentSearchesUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<SearchUiState, SearchUiEffect>(SearchUiState(), dispatcherProvider),
    SearchInteractionListener,
    FilterInteractionListener {
    private val _keyword = MutableStateFlow("")

    init {
        fetchRecentSearches()
        observeSearchKeywordChanges()
    }

    private fun fetchRecentSearches() {
        startLoading()
        tryToExecute(
            action = { recentSearchesUseCase.getRecentSearches() },
            onSuccess = ::onLoadRecentSearchesSuccess,
            onError = ::onFetchError,
        )
    }

    private fun onLoadRecentSearchesSuccess(recentSearches: List<String>) {
        updateState { it.copy(recentSearches = recentSearches, errorUiState = null) }
    }

    private fun observeSearchKeywordChanges() {
        viewModelScope.launch { _keyword.debounceSearch(::onSearchKeywordChanged) }
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
                ).flow.map { pagingData -> pagingData.map { it.toMediaItemUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = ::onFetchMoviesSuccess,
            onError = {},
        )
    }

    private fun onFetchMoviesSuccess(movies: Flow<PagingData<MovieItemUiState>>) {
        applyMoviesFilter()
        updateState { it.copy(movies = movies) }
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
                ).flow.map { pagingData -> pagingData.map { it.toMediaItemUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = ::onFetchTvShowsSuccess,
            onError = {},
        )
    }

    private fun onFetchTvShowsSuccess(tvShows: Flow<PagingData<TvShowItemUiState>>) {
        applyTvShowsFilter()
        updateState { it.copy(tvShows = tvShows) }
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
                ).flow.map { pagingData -> pagingData.map { it.toMediaItemUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = ::onMoviesFilteredSuccess,
            onError = ::onFetchError,
            onCompletion = ::onClickCancel,
        )
    }

    private fun onMoviesFilteredSuccess(movies: Flow<PagingData<MovieItemUiState>>) {
        updateState {
            it.copy(
                movies = movies,
                filterItemUiState = it.filterItemUiState.copy(isLoading = false),
            )
        }
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
                ).flow
                    .map { pagingData ->
                        pagingData.map { it.toMediaItemUiState() }
                    }.cachedIn(viewModelScope)
            },
            onSuccess = ::onTvShowsFilteredSuccess,
            onError = ::onFetchError,
            onCompletion = ::onClickCancel,
        )
    }

    private fun onTvShowsFilteredSuccess(tvShows: Flow<PagingData<TvShowItemUiState>>) {
        updateState {
            it.copy(
                tvShows = tvShows,
                filterItemUiState = it.filterItemUiState.copy(isLoading = false),
            )
        }
    }

    private fun onFetchError(exception: AflamiException) {
        updateState { it.copy(errorUiState = SearchErrorState.toSearchErrorState(exception)) }
    }

    private fun resetFilterState() = updateState { it.copy(filterItemUiState = FilterItemUiState()) }

    private fun startLoading() = updateState { it.copy(isLoading = true) }

    override fun onChangeSearchKeyword(keyword: String) {
        _keyword.update { oldText -> keyword }
        updateState { it.copy(keyword = keyword) }
    }

    override fun onSaveSearchHistory() {
        if (state.value.keyword.isBlank()) return
        tryToExecute(
            action = { recentSearchesUseCase.addRecentSearch(state.value.keyword) },
            onSuccess = { fetchRecentSearches() },
            onError = ::onFetchError,
        )
    }

    override fun onClickNavigateBack() {
        if (state.value.keyword.isNotEmpty()) {
            onSaveSearchHistory()
            onClickClearSearch()
        } else {
            sendNewEffect(SearchUiEffect.NavigateBack)
        }
    }

    override fun onClickWorldSearchCard() = sendNewEffect(SearchUiEffect.NavigateToWorldSearch)

    override fun onClickActorSearchCard() = sendNewEffect(SearchUiEffect.NavigateToActorSearch)

    override fun onClickRetryRequest() = onSearchKeywordChanged(_keyword.value)

    override fun onClickTabOption(tabOption: TabOption) {
        updateState {
            it.copy(
                selectedTabOption = tabOption,
                movies = state.value.movies,
                tvShows = state.value.tvShows,
                isLoading = true,
                filterItemUiState = FilterItemUiState(),
            )
        }
        onSearchKeywordChanged(_keyword.value)
    }

    override fun onClickRecentSearch(keyword: String) = onChangeSearchKeyword(keyword)

    override fun onClickClearRecentSearch(keyword: String) {
        startLoading()
        tryToExecute(
            action = { recentSearchesUseCase.deleteRecentSearch(searchKeyword = keyword) },
            onSuccess = { fetchRecentSearches() },
            onError = ::onFetchError,
        )
    }

    override fun onClickClearAllRecentSearches() {
        startLoading()
        tryToExecute(
            action = { recentSearchesUseCase.deleteRecentSearches() },
            onSuccess = ::onClearAllRecentSearchesSuccess,
            onError = ::onFetchError,
        )
    }

    private fun onClearAllRecentSearchesSuccess(unit: Unit) {
        updateState { it.copy(recentSearches = emptyList()) }
    }

    override fun onClickClearSearch() {
        updateState { currentState ->
            currentState.copy(
                keyword = "",
                isDialogVisible = false,
                filterItemUiState = FilterItemUiState(),
            )
        }
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (val refreshState = loadStates.refresh) {
            is LoadState.Loading -> {
                if (state.value.keyword.isNotBlank()) {
                    updateState { it.copy(isLoading = true, errorUiState = null) }
                }
            }

            is LoadState.NotLoading -> {
                updateState { it.copy(isLoading = false) }
            }

            is LoadState.Error -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorUiState = SearchErrorState.toSearchErrorState(
                            refreshState.error
                        ),
                    )
                }
            }
        }
    }

    override fun onClickMovieCard(movieId: Long) {
        sendNewEffect(SearchUiEffect.NavigateToMovieDetails(movieId))
    }

    override fun onClickTvShowCard(tvShowId: Long) {
        sendNewEffect(SearchUiEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onClickFilterButton() {
        updateState { it.copy(isDialogVisible = true, isLoading = false) }
    }

    override fun onChangeRatingStar(ratingIndex: Int) {
        updateState { it.copy(filterItemUiState = it.filterItemUiState.copy(selectedStarIndex = ratingIndex)) }
    }

    override fun onChangeMovieGenre(genreType: MovieGenre) {
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

    override fun onChangeTvShowGenre(genreType: TvShowGenre) {
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

    override fun onClickCancel() {
        updateState {
            it.copy(
                isDialogVisible = false,
                filterItemUiState = it.filterItemUiState.copy(isLoading = false),
            )
        }
    }

    override fun onClickApply() {
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

    override fun onClickClear() = resetFilterState()
}
