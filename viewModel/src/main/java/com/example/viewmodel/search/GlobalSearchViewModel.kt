package com.example.viewmodel.search

import com.example.domain.exceptions.QueryTooShortException
import com.example.domain.usecase.GetMoviesByKeywordUseCase
import com.example.domain.usecase.GetTvShowByKeywordUseCase
import com.example.domain.usecase.search.ClearAllRecentSearchesUseCase
import com.example.domain.usecase.search.ClearRecentSearchUseCase
import com.example.domain.usecase.search.GetRecentSearchesUseCase
import com.example.viewmodel.BaseViewModel
import com.example.viewmodel.common.GenreType
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.common.toMediaItemUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

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
        _query.debounce(300)
            .map { it.trim() }
            .onEach {
                if (it.length <= 3) {
                    throw QueryTooShortException()
                }
                when (state.value.selectedTabOption) {
                    TabOption.MOVIES -> fetchMoviesByQuery(it)
                    TabOption.TV_SHOWS -> fetchTvShowsByQuery(it)
                }
            }
    }

    override fun onNavigateBackClicked() {
        sendNewEffect(SearchUiEffect.NavigateBack)
    }

    override fun onTextValuedChanged(text: String) {
        updateState {
            it.copy(
                query = text,
            )
        }
    }

    private fun fetchMoviesByQuery(keyword: String) {
        tryToExecute(
            action = { getMoviesByKeywordUseCase(keyword = keyword) },
            onSuccess = { movies ->
                updateState {
                    it.copy(movies = movies.map { movie -> movie.toMediaItemUiState() })
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        errorUiState = mapToSearchUiState(exception)
                    )
                }
            },
        )
    }

    private fun fetchTvShowsByQuery(keyword: String) {
        tryToExecute(
            action = { getTvShowByKeywordUseCase(keyword = keyword) },
            onSuccess = { tvShows ->
                updateState {
                    it.copy(tvShows = tvShows.map { tvShow -> tvShow.toMediaItemUiState() })
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        errorUiState = mapToSearchUiState(exception)
                    )
                }
            },
        )
    }

    override fun onFilterButtonClicked() {
        updateState {
            it.copy(
                isDialogVisible = true,
            )
        }
    }

    override fun onWorldSearchCardClicked() {
        sendNewEffect(SearchUiEffect.NavigateToWorldSearch)
    }

    override fun onActorSearchCardClicked() {
        sendNewEffect(SearchUiEffect.NavigateToActorSearch)
    }

    override fun onTabOptionClicked(tabOption: TabOption) {
        updateState {
            it.copy(
                selectedTabOption = tabOption,
                movies = state.value.movies,
                tvShows = state.value.tvShows,
            )
        }
    }

    override fun onMovieCardClicked() {
        sendNewEffect(SearchUiEffect.NavigateToMovieDetails)
    }

    override fun onRecentSearchClicked(keyword: String) {
        onTextValuedChanged(keyword)
    }

    override fun onClearRecentSearch(keyword: String) {
        updateState {
            it.copy(isLoading = true)
        }
        tryToExecute(
            action = {
                clearRecentSearchUseCase(searchKeyword = keyword)
            },
            onSuccess = { loadRecentSearches() },
            onError = { exception ->
                updateState {
                    it.copy(
                        errorUiState = mapToSearchUiState(exception),
                        isLoading = false
                    )
                }
            },
        )
    }

    override fun onClearAllRecentSearches() {
        updateState {
            it.copy(isLoading = true)
        }
        tryToExecute(
            action = { clearAllRecentSearchesUseCase() },
            onSuccess = {
                updateState {
                    it.copy(
                        recentSearches = emptyList(),
                        isLoading = false
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        errorUiState = mapToSearchUiState(exception),
                        isLoading = false
                    )
                }
            },
        )
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
                filterItemUiState = it.filterItemUiState.copy(
                    selectedStarIndex = ratingIndex
                )
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
        updateState {
            it.copy(isLoading = true)
        }
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
            onSuccess = { filteredMovies ->
                updateState {
                    it.copy(
                        movies = filteredMovies.map { movie -> movie.toMediaItemUiState() },
                        isLoading = false,
                        isDialogVisible = false
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        errorUiState = mapToSearchUiState(exception),
                        isLoading = false,
                        isDialogVisible = false
                    )
                }
            },
        )
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
            onSuccess = { filteredTvShows ->
                updateState {
                    it.copy(
                        movies = filteredTvShows.map { tvShow -> tvShow.toMediaItemUiState() },
                        isLoading = false
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        errorUiState = mapToSearchUiState(exception),
                        isLoading = false
                    )
                }
            },
        )
    }

    override fun onClearButtonClicked() {
        updateState {
            it.copy(
                filterItemUiState = FilterItemUiState()
            )
        }
    }

    private fun loadRecentSearches() {
        updateState {
            it.copy(isLoading = true)
        }
        tryToExecute(
            action = { getRecentSearchesUseCase() },
            onSuccess = { recentSearches ->
                updateState {
                    it.copy(
                        recentSearches = recentSearches,
                        isLoading = false
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        errorUiState = mapToSearchUiState(exception),
                        isLoading = false
                    )
                }
            },
        )
    }

}