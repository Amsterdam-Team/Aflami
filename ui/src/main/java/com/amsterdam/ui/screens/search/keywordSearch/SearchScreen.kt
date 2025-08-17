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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.LoadingContainer
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
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiState.SearchErrorState
import com.amsterdam.viewmodel.search.keywordSearch.SearchViewModel
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.TabOption
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
        movies = movieFlow,
        tvShows = tvShowFlow,
        interaction = viewModel,
        filterInteraction = viewModel,
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun SearchContent(
    state: SearchUiState,
    movies: LazyPagingItems<SearchMediaItemUiState>,
    tvShows: LazyPagingItems<SearchMediaItemUiState>,
    interaction: SearchInteractionListener,
    filterInteraction: FilterInteractionListener,
) {
    BackHandler(enabled = state.keyword.isNotEmpty()) {
        interaction.onClickClearSearch()
    }
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
                onNavigateBackClicked = interaction::onClickNavigateBack,
                onKeywordValuedChanged = interaction::onChangeSearchKeyword,
                onFilterButtonClicked = interaction::onClickFilterButton,
                onTabOptionClicked = interaction::onClickTabOption,
                onSaveSearchHistory = interaction::onSaveSearchHistory,
                onHeaderSizeChanged = { headerHeight = it.height.dp },
                keyboardController = keyboardController
            )

            AnimatedVisibility(state.keyword.isBlank()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    SuggestionsHubSection(
                        onWorldSearchCardClicked = interaction::onClickWorldSearchCard,
                        onActorSearchCardClicked = interaction::onClickActorSearchCard
                    )

                    RecentSearchesSection(
                        state = state,
                        onAllRecentSearchesCleared = interaction::onClickClearAllRecentSearches,
                        onRecentSearchClicked = interaction::onClickRecentSearch,
                        onRecentSearchCleared = interaction::onClickClearRecentSearch
                    )
                }
            }

            val selectedItems = if (state.selectedTabOption == TabOption.MOVIES) {
                movies
            } else {
                tvShows
            }

            AnimatedVisibility(state.keyword.isNotBlank()) {
                AnimatedVisibility(
                    selectedItems.itemCount > 0
                ) {
                    SuccessMediaItemsSection(
                        onMovieClicked = interaction::onClickMovieCard,
                        onTvShowClicked = interaction::onClickTvShowCard,
                        selectedItems = selectedItems
                    )
                }
                val isSelectedTabHasNoData = if (state.selectedTabOption == TabOption.MOVIES) {
                    movies.itemSnapshotList.isEmpty()
                } else {
                    tvShows.itemSnapshotList.isEmpty()
                }
                AnimatedVisibility(state.isLoading || state.errorUiState != null || isSelectedTabHasNoData) {
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
                                state.isLoading && state.errorUiState == null -> {
                                    LoadingContainer()
                                }

                                state.errorUiState is SearchErrorState.NoNetworkConnection -> {
                                    NoNetworkContainer(
                                        onClickRetry = interaction::onClickRetryRequest
                                    )
                                }

                                !state.isLoading && state.errorUiState == null && isSelectedTabHasNoData -> {
                                    NoDataContainer(
                                        imageRes = painterResource(com.amsterdam.ui.R.drawable.placeholder_no_result_found),
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
            modifier = Modifier.fillMaxSize(),
            visible = state.isDialogVisible
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