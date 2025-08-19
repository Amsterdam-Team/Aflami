package com.amsterdam.viewmodel.search.keywordSearch

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterMoviesByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterTvShowsByKeywordUseCase
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.paging.createPagingSource
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiState.FilterItemUiState
import com.amsterdam.viewmodel.search.mapper.getSelectedGenreType
import com.amsterdam.viewmodel.search.mapper.selectByMovieGenre
import com.amsterdam.viewmodel.search.mapper.selectByTvGenre
import com.amsterdam.viewmodel.search.mapper.toSearchMediaItemUiState
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.utils.debounceSearch
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAndFilterMoviesByKeywordUseCase: GetAndFilterMoviesByKeywordUseCase,
    private val getAndFilterTvShowsByKeywordUseCase: GetAndFilterTvShowsByKeywordUseCase,
    private val recentSearchesUseCase: RecentSearchesUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<SearchUiState, SearchUiEffect>(SearchUiState(), dispatcherProvider),
    SearchInteractionListener,
    FilterInteractionListener {
    private val _keyword = MutableStateFlow("")

    init {
        manageLocaleLanguageUseCase.getAppLanguage()
                .onEach {
                    observeSearchKeywordChanges()
                }.launchIn(viewModelScope)

        getRecentSearches()
        observeSearchKeywordChanges()
    }

    private fun getRecentSearches(startLoading: Boolean = true) {
        startLoading(startLoading)
        tryToExecute(
            action = recentSearchesUseCase::getRecentSearches,
            onSuccess = ::onGetRecentSearchesSuccess,
            onCompletion = ::onGetCompletion
        )
    }

    private fun onGetRecentSearchesSuccess(recentSearches: List<String>) {
        resetErrorStateToNull()
        updateState { it.copy(recentSearches = recentSearches, isLoading = false) }
    }

    private fun onGetCompletion() {
        updateState { it.copy(isLoading = false) }
    }

    private fun observeSearchKeywordChanges() {
        viewModelScope.launch { _keyword.debounceSearch(::onSearchKeywordChanged) }
    }

    private fun onSearchKeywordChanged(keyword: String) {
        when (state.value.selectedTabOption) {
            TabOption.MOVIES -> getMoviesByKeyword(keyword)
            TabOption.TV_SHOWS -> getTvShowsByKeyword(keyword)
        }
    }

    private fun getMoviesByKeyword(keyword: String) {
        startLoading()
        tryToExecute(
            action = {
                createPagingSource(scope = viewModelScope) { page ->
                    getAndFilterMoviesByKeywordUseCase(
                        keyword = keyword,
                        page = page,
                        rating = state.value.movieFilterItemUiState.selectedStarIndex,
                        movieGenre = state.value.movieFilterItemUiState.selectableMovieGenres.getSelectedGenreType()
                    ).map { it.toSearchMediaItemUiState() }
                }
            },
            onSuccess = ::onGetMoviesSuccess
        )
    }

    private fun onGetMoviesSuccess(movies: Flow<PagingData<SearchMediaItemUiState>>) {
        updateState { it.copy(movies = movies) }
    }

    private fun getTvShowsByKeyword(keyword: String) {
        startLoading()
        tryToExecute(
            action = {
                createPagingSource(scope = viewModelScope) { page ->
                    getAndFilterTvShowsByKeywordUseCase(
                        keyword = keyword,
                        page = page,
                        rating = state.value.tvShowFilterItemUiState.selectedStarIndex,
                        tvGenre = state.value.tvShowFilterItemUiState.selectableTvShowGenres.getSelectedGenreType()
                    ).map { it.toSearchMediaItemUiState() }
                }
            },
            onSuccess = ::onGetTvShowsSuccess
        )
    }

    private fun onGetTvShowsSuccess(tvShows: Flow<PagingData<SearchMediaItemUiState>>) {
        updateState { it.copy(tvShows = tvShows) }
    }

    private fun applyMoviesFilter() {
        val currentMovieFilterState = state.value.movieFilterItemUiState
        tryToExecute(
            action = {
                createPagingSource(scope = viewModelScope) { page ->
                    getAndFilterMoviesByKeywordUseCase(
                        keyword = state.value.keyword,
                        page = page,
                        rating = currentMovieFilterState.selectedStarIndex,
                        movieGenre = currentMovieFilterState.selectableMovieGenres.getSelectedGenreType(),
                    ).map { it.toSearchMediaItemUiState() }
                }
            },
            onSuccess = ::onMoviesFilteredSuccess,
            onCompletion = ::onClickCancel,
        )
    }

    private fun onMoviesFilteredSuccess(movies: Flow<PagingData<SearchMediaItemUiState>>) {
        updateState {
            it.copy(
                movies = movies,
                movieFilterItemUiState = it.movieFilterItemUiState.copy(isLoading = false),
            )
        }
    }

    private fun applyTvShowsFilter() {
        val currentTvShowFilterState = state.value.tvShowFilterItemUiState
        tryToExecute(
            action = {
                createPagingSource(scope = viewModelScope) { page ->
                    getAndFilterTvShowsByKeywordUseCase(
                        keyword = state.value.keyword,
                        page = page,
                        rating = currentTvShowFilterState.selectedStarIndex,
                        tvGenre = currentTvShowFilterState.selectableTvShowGenres.getSelectedGenreType(),
                    ).map { it.toSearchMediaItemUiState() }
                }
            },
            onSuccess = ::onTvShowsFilteredSuccess,
            onError = ::onGetError,
            onCompletion = ::onClickCancel,
        )
    }

    private fun onTvShowsFilteredSuccess(tvShows: Flow<PagingData<SearchMediaItemUiState>>) {
        updateState {
            it.copy(
                tvShows = tvShows,
                tvShowFilterItemUiState = it.tvShowFilterItemUiState.copy(isLoading = false),
            )
        }
    }

    private fun onGetError(exception: AflamiException) {
        updateState { it.copy(isLoading = false) }
    }

    private fun resetFilterState() {
        updateState { currentState ->
            when (currentState.selectedTabOption) {
                TabOption.MOVIES -> currentState.copy(movieFilterItemUiState = FilterItemUiState())
                TabOption.TV_SHOWS -> currentState.copy(tvShowFilterItemUiState = FilterItemUiState())
            }
        }
    }

    private fun startLoading(start: Boolean = true) = updateState { it.copy(isLoading = start) }

    override fun onChangeSearchKeyword(keyword: String) {
        if (keyword.trim() != state.value.keyword.trim() && keyword.isNotBlank()) {
            _keyword.update { keyword }
        }
        if (keyword.isBlank()) {
            resetErrorStateToNull()
            updateState { it.copy(movies = emptyFlow(), tvShows = emptyFlow(), isLoading = false) }
        }
        updateState { it.copy(keyword = keyword) }
    }

    override fun onSaveSearchHistory() {
        val keyword = state.value.keyword
        if (keyword.isBlank()) return
        tryToExecute(
            action = { recentSearchesUseCase.addRecentSearch(keyword) },
            onSuccess = { getRecentSearches() },
            onError = ::onGetError,
        )
    }

    override fun onClickNavigateBack() {
        if (state.value.keyword.isNotBlank()) {
            onSaveSearchHistory()
            onClickClearSearch()
        } else {
            sendNewNavigationEffect(SearchUiEffect.NavigateBack)
        }
    }

    override fun onClickWorldSearchCard() {
        sendNewNavigationEffect(SearchUiEffect.NavigateToWorldSearch)
    }

    override fun onClickActorSearchCard() {
        sendNewNavigationEffect(SearchUiEffect.NavigateToActorSearch)
    }

    override fun onClickRetryRequest() = onSearchKeywordChanged(_keyword.value)

    override fun onClickTabOption(tabOption: TabOption) {
        updateState {
            it.copy(
                selectedTabOption = tabOption,
                isLoading = true,
            )
        }
        onSearchKeywordChanged(_keyword.value)
    }

    override fun onClickRecentSearch(keyword: String){
        startLoading(true)
        onChangeSearchKeyword(keyword)
    }

    override fun onClickClearRecentSearch(keyword: String) {
        tryToExecute(
            action = { recentSearchesUseCase.deleteRecentSearch(searchKeyword = keyword) },
            onSuccess = { getRecentSearches(startLoading = false) },
            onError = ::onGetError,
        )
    }

    override fun onClickClearAllRecentSearches() {
        startLoading()
        tryToExecute(
            action = { recentSearchesUseCase.deleteRecentSearches() },
            onSuccess = ::onClearAllRecentSearchesSuccess,
            onError = ::onGetError,
        )
    }

    private fun onClearAllRecentSearchesSuccess(unit: Unit) {
        updateState { it.copy(recentSearches = emptyList(), isLoading = false) }
    }

    override fun onClickClearSearch() {
        _keyword.value = ""
        updateState { currentState ->
            currentState.copy(
                keyword = "",
                isDialogVisible = false,
                movieFilterItemUiState = FilterItemUiState(),
                tvShowFilterItemUiState = FilterItemUiState(),
                isLoading = false,
                movies = emptyFlow(),
                tvShows = emptyFlow(),
            )
        }
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (val refreshState = loadStates.refresh) {
            is LoadState.Loading -> {
                if (state.value.keyword.isNotBlank()) {
                    resetErrorStateToNull()
                    updateState { it.copy(isLoading = true) }
                }
            }

            is LoadState.NotLoading -> {
                updateState { it.copy(isLoading = false) }
            }

            is LoadState.Error -> {
                updateErrorStateByException(AflamiException())
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    override fun onClickMovieCard(movieId: Long) {
        sendNewNavigationEffect(SearchUiEffect.NavigateToMovieDetails(movieId))
    }

    override fun onClickTvShowCard(tvShowId: Long) {
        sendNewNavigationEffect(SearchUiEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onClickFilterButton() {
        updateState { it.copy(isDialogVisible = true, isLoading = false) }
    }

    override fun onChangeRatingStar(ratingIndex: Int) {
        updateState { currentState ->
            when (currentState.selectedTabOption) {
                TabOption.MOVIES -> currentState.copy(
                    movieFilterItemUiState = currentState.movieFilterItemUiState.copy(
                        selectedStarIndex = ratingIndex
                    )
                )

                TabOption.TV_SHOWS -> currentState.copy(
                    tvShowFilterItemUiState = currentState.tvShowFilterItemUiState.copy(
                        selectedStarIndex = ratingIndex
                    )
                )
            }
        }
    }

    override fun onChangeMovieGenre(genreType: MovieGenre) {
        updateState {
            it.copy(
                movieFilterItemUiState =
                    state.value.movieFilterItemUiState.copy(
                        selectableMovieGenres =
                            it.movieFilterItemUiState.selectableMovieGenres.selectByMovieGenre(
                                genreType,
                            ),
                    ),
            )
        }
    }

    override fun onChangeTvShowGenre(genreType: TvShowGenre) {
        updateState {
            it.copy(
                tvShowFilterItemUiState =
                    state.value.tvShowFilterItemUiState.copy(
                        selectableTvShowGenres =
                            it.tvShowFilterItemUiState.selectableTvShowGenres.selectByTvGenre(
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
                movieFilterItemUiState = it.movieFilterItemUiState.copy(isLoading = false),
                tvShowFilterItemUiState = it.tvShowFilterItemUiState.copy(isLoading = false),
            )
        }
    }

    override fun onClickApply() {
        updateState { currentState ->
            currentState.copy(
                isDialogVisible = false,
                movieFilterItemUiState = if (currentState.selectedTabOption == TabOption.MOVIES) currentState.movieFilterItemUiState.copy(
                    isLoading = true
                ) else currentState.movieFilterItemUiState,
                tvShowFilterItemUiState = if (currentState.selectedTabOption == TabOption.TV_SHOWS) currentState.tvShowFilterItemUiState.copy(
                    isLoading = true
                ) else currentState.tvShowFilterItemUiState,
            )
        }

        when (state.value.selectedTabOption) {
            TabOption.MOVIES -> applyMoviesFilter()
            TabOption.TV_SHOWS -> applyTvShowsFilter()
        }
    }

    override fun onClickClear() = resetFilterState()
}