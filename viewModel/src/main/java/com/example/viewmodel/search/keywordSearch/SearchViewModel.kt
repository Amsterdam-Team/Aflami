package com.example.viewmodel.search.keywordSearch

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
import com.example.domain.useCase.RecentSearchesUseCase
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.paging.PagingSource
import com.example.viewmodel.search.mapper.getSelectedGenreType
import com.example.viewmodel.search.mapper.selectByMovieGenre
import com.example.viewmodel.search.mapper.selectByTvGenre
import com.example.viewmodel.search.mapper.toMediaItemUiState
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import com.example.viewmodel.shared.uiStates.TvShowItemUiState
import com.example.viewmodel.utils.debounceSearch
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
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
            onCompletion = ::onCompletion,
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
            onError = ::onFetchError,
            onCompletion = ::onCompletion,
        )
    }

    private fun onFetchMoviesSuccess(movies: Flow<PagingData<MovieItemUiState>>) {
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
                ).flow.map { pagingData -> pagingData.map { it.toMediaItemUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = ::onFetchTvShowsSuccess,
            onError = ::onFetchError,
            onCompletion = ::onCompletion,
        )
    }

    private fun onFetchTvShowsSuccess(tvShows: Flow<PagingData<TvShowItemUiState>>) {
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
                errorUiState = null,
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
                errorUiState = null,
            )
        }
    }

    private fun onFetchError(exception: AflamiException) {
        updateState { it.copy(errorUiState = SearchErrorState.toSearchErrorState(exception)) }
    }

    private fun resetFilterState() = updateState { it.copy(filterItemUiState = FilterItemUiState()) }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }

    private fun startLoading() = updateState { it.copy(isLoading = true) }

    override fun onChangeSearchKeyword(keyword: String) {
        _keyword.update { oldText -> keyword }
        updateState { it.copy(keyword = keyword) }
    }

    override fun onClickSearchAction() {
        onChangeSearchKeyword(state.value.keyword)
        tryToExecute(
            action = { recentSearchesUseCase.addRecentSearch(state.value.keyword) },
            onSuccess = { fetchRecentSearches() },
            onError = ::onFetchError,
            onCompletion = ::onCompletion,
        )
    }

    override fun onClickNavigateBack() {
        if (state.value.keyword.isNotEmpty()) {
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
                isLoading = true, // TODO: Check
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
            onCompletion = ::onCompletion,
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
