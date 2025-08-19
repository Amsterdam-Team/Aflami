package com.amsterdam.ui.screens.search.keywordSearch

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.screens.search.keywordSearch.sections.RecentSearchesSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.SearchScreenHeaderSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.SuccessMediaItemsSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.SuggestionsHubSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.FilterDialog
import com.amsterdam.viewmodel.search.keywordSearch.FilterInteractionListener
import com.amsterdam.viewmodel.search.keywordSearch.SearchInteractionListener
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiEffect
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiState
import com.amsterdam.viewmodel.search.keywordSearch.SearchViewModel
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.NoInternetError
import com.amsterdam.viewmodel.shared.errorUiState.isNull
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val movieFlow = state.movies.collectAsLazyPagingItems()
    val tvShowFlow = state.tvShows.collectAsLazyPagingItems()
    val navigationManager = LocalNavManager.current
    LaunchedEffect(movieFlow.loadState) {
        if (state.selectedTabOption == TabOption.MOVIES) {
            viewModel.onPagingLoadStateChanged(movieFlow.loadState)
        }
    }
    LaunchedEffect(tvShowFlow.loadState) {
        if (state.selectedTabOption == TabOption.TV_SHOWS) {
            viewModel.onPagingLoadStateChanged(tvShowFlow.loadState)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SearchUiEffect.NavigateBack -> {
                    navigationManager.navigateUp()
                }

                SearchUiEffect.NavigateToActorSearch -> {
                    navigationManager.toSearchByActor()
                }

                SearchUiEffect.NavigateToWorldSearch -> {
                    navigationManager.toSearchByCountry()
                }

                is SearchUiEffect.NavigateToMovieDetails -> {
                    viewModel.onSaveSearchHistory()
                    navigationManager.toMovieDetails(effect.movieId)
                }

                is SearchUiEffect.NavigateToTvShowDetails -> {
                    viewModel.onSaveSearchHistory()
                    navigationManager.toSeriesDetails(effect.tvShowId)
                }
            }
        }
    }


    SearchContent(
        state = state,
        errorState = errorState,
        movies = movieFlow,
        tvShows = tvShowFlow,
        interactionListener = viewModel,
        filterInteraction = viewModel,
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun SearchContent(
    state: SearchUiState,
    errorState: ErrorUiState?,
    movies: LazyPagingItems<SearchMediaItemUiState>,
    tvShows: LazyPagingItems<SearchMediaItemUiState>,
    interactionListener: SearchInteractionListener,
    filterInteraction: FilterInteractionListener,
) {
    BackHandler(enabled = state.keyword.isNotEmpty()) {
        interactionListener.onClickClearSearch()
    }
    val density = LocalDensity.current
    var headerHeight by remember { mutableStateOf(0.dp) }
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            SearchScreenHeaderSection(
                keyword = state.keyword,
                selectedTabOption = state.selectedTabOption,
                onNavigateBackClicked = interactionListener::onClickNavigateBack,
                onKeywordValuedChanged = interactionListener::onChangeSearchKeyword,
                onFilterButtonClicked = interactionListener::onClickFilterButton,
                onTabOptionClicked = interactionListener::onClickTabOption,
                onSaveSearchHistory = interactionListener::onSaveSearchHistory,
                onHeaderSizeChanged = { headerHeight = with(density) { it.height.dp } },
                keyboardController = keyboardController
            )

            AnimatedVisibility(state.keyword.isBlank()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    SuggestionsHubSection(
                        onWorldSearchCardClicked = interactionListener::onClickWorldSearchCard,
                        onActorSearchCardClicked = interactionListener::onClickActorSearchCard
                    )

                    RecentSearchesSection(
                        recentSearches = state.recentSearches,
                        isLoading = state.isLoading,
                        keyword = state.keyword.trim(),
                        onAllRecentSearchesCleared = interactionListener::onClickClearAllRecentSearches,
                        onRecentSearchClicked = interactionListener::onClickRecentSearch,
                        onRecentSearchCleared = interactionListener::onClickClearRecentSearch
                    )
                }
            }

            val selectedItems = if (state.selectedTabOption == TabOption.MOVIES) {
                movies
            } else {
                tvShows
            }

            AnimatedVisibility(state.keyword.isNotBlank()) {
                AnimatedVisibility(selectedItems.itemCount > 0) {
                    SuccessMediaItemsSection(
                        onMovieClicked = interactionListener::onClickMovieCard,
                        onTvShowClicked = interactionListener::onClickTvShowCard,
                        selectedItems = selectedItems
                    )
                }
                val isSelectedTabHasNoData = if (state.selectedTabOption == TabOption.MOVIES) {
                    movies.itemSnapshotList.isEmpty()
                } else {
                    tvShows.itemSnapshotList.isEmpty()
                }
                AnimatedVisibility(state.isLoading || !errorState.isNull() || isSelectedTabHasNoData) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CenterOfScreenContainer(
                            unneededSpace = headerHeight,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            when {
                                state.isLoading && errorState.isNull() -> {
                                    LoadingContainer()
                                }

                                errorState is NoInternetError -> {
                                    NoNetworkContainer(onClickRetry = interactionListener::onClickRetryRequest)
                                }

                                !state.isLoading && errorState.isNull() && isSelectedTabHasNoData -> {
                                    NoDataContainer(
                                        imageRes = painterResource(R.drawable.placeholder_no_result_found),
                                        title = stringResource(R.string.no_search_result),
                                        description = stringResource(R.string.no_search_result_description),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(), visible = state.isDialogVisible
        ) {
            val currentFilterState = if (state.selectedTabOption == TabOption.MOVIES) {
                state.movieFilterItemUiState
            } else {
                state.tvShowFilterItemUiState
            }
            FilterDialog(
                filterState = currentFilterState,
                selectedTabOption = state.selectedTabOption,
                interaction = filterInteraction
            )
        }
    }
}