package com.amsterdam.ui.screens.search.keywordSearch

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.TabsLayout
import com.amsterdam.designsystem.components.TextField
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.MovieCard
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.navigation.Route.MovieDetails
import com.amsterdam.ui.navigation.Route.SeriesDetails
import com.amsterdam.ui.screens.search.keywordSearch.sections.ExploreMoviesAndShows
import com.amsterdam.ui.screens.search.keywordSearch.sections.RecentSearchesSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.SuggestionsHubSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.FilterDialog
import com.amsterdam.ui.utils.safeNavigate
import com.amsterdam.viewmodel.search.keywordSearch.FilterInteractionListener
import com.amsterdam.viewmodel.search.keywordSearch.SearchErrorState
import com.amsterdam.viewmodel.search.keywordSearch.SearchInteractionListener
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiEffect
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiState
import com.amsterdam.viewmodel.search.keywordSearch.SearchViewModel
import com.amsterdam.viewmodel.search.keywordSearch.TabOption
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SearchScreen(viewModel: SearchViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val movieFlow = state.movies.collectAsLazyPagingItems()
    val tvShowFlow = state.tvShows.collectAsLazyPagingItems()
    val navController = LocalNavController.current
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
            effect?.let {
                when (effect) {
                    SearchUiEffect.NavigateBack -> navController.popBackStack()
                    SearchUiEffect.NavigateToActorSearch -> navController.safeNavigate(Route.SearchByActor)
                    SearchUiEffect.NavigateToWorldSearch -> navController.safeNavigate(Route.SearchByCountry)
                    is SearchUiEffect.NavigateToMovieDetails -> {
                        viewModel.onSaveSearchHistory()
                        navController.safeNavigate(MovieDetails(effect.movieId))
                    }
                    is SearchUiEffect.NavigateToTvShowDetails -> {
                        viewModel.onSaveSearchHistory()
                        navController.navigate(SeriesDetails(effect.tvShowId))
                    }
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

@Composable
private fun SearchContent(
    state: SearchUiState,
    movies: LazyPagingItems<MovieItemUiState>,
    tvShows: LazyPagingItems<TvShowItemUiState>,
    interaction: SearchInteractionListener,
    filterInteraction: FilterInteractionListener,
) {
    BackHandler(enabled = state.keyword.isNotEmpty()) {
        interaction.onClickClearSearch()
    }
    var headerHeight by remember { mutableStateOf(0.dp) }
    var isPageStillLoading by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = AppTheme.color.surface)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        SearchScreenHeader(
            keyword = state.keyword,
            selectedTabOption = state.selectedTabOption,
            onNavigateBackClicked = interaction::onClickNavigateBack,
            onKeywordValuedChanged = interaction::onChangeSearchKeyword,
            onFilterButtonClicked = interaction::onClickFilterButton,
            onTabOptionClicked = interaction::onClickTabOption,
            onSaveSearchHistory = interaction::onSaveSearchHistory,
            onHeaderSizeChanged = {
                headerHeight = it.height.dp
            },
        )

        AnimatedVisibility(
            (state.isLoading || isPageStillLoading) && state.keyword.isNotBlank() && state.errorUiState == null,
        ) {
            CenterOfScreenContainer(unneededSpace = headerHeight) {
                LoadingContainer()
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            visible = state.isDialogVisible,
        ) {
            FilterDialog(
                filterState = state.filterItemUiState,
                selectedTabOption = state.selectedTabOption,
                interaction = filterInteraction
            )
        }

        AnimatedVisibility(state.keyword.isNotBlank() && state.errorUiState == null) {
            SuccessMediaItems(
                selectedTabOption = state.selectedTabOption,
                moviesFlow = movies,
                tvShowsFlow = tvShows,
                onPageLoading = { isPageStillLoading = it },
                onMovieClicked = interaction::onClickMovieCard,
                onTvShowClicked = interaction::onClickTvShowCard,
            )
        }

        SuggestionsHubSection(
            keyword = state.keyword,
            onWorldSearchCardClicked = interaction::onClickWorldSearchCard,
            onActorSearchCardClicked = interaction::onClickActorSearchCard
        )

        AnimatedVisibility(
            state.keyword.isEmpty() && state.recentSearches.isEmpty()
        ) {
            CenterOfScreenContainer(unneededSpace = headerHeight + 100.dp) {
                ExploreMoviesAndShows()
            }
        }

        RecentSearchesSection(
            keyword = state.keyword,
            recentSearches = state.recentSearches,
            onAllRecentSearchesCleared = interaction::onClickClearAllRecentSearches,
            onRecentSearchClicked = interaction::onClickRecentSearch,
            onRecentSearchCleared = interaction::onClickClearRecentSearch
        )

        CenterOfScreenContainer(
            unneededSpace = headerHeight,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            AnimatedVisibility(state.keyword.isNotBlank()) {
                if (state.errorUiState != null && state.errorUiState is SearchErrorState.NoNetworkConnection) {
                    NoNetworkContainer(
                        onClickRetry = interaction::onClickRetryRequest,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }

                val isSelectedTabSearchResultEmpty =
                    if (state.selectedTabOption == TabOption.MOVIES) {
                        movies.itemSnapshotList.isEmpty()
                    } else {
                        tvShows.itemSnapshotList.isEmpty()
                    }

                AnimatedVisibility(state.errorUiState == null && isSelectedTabSearchResultEmpty) {
                    NoDataContainer(
                        imageRes = painterResource(com.amsterdam.ui.R.drawable.placeholder_no_result_found),
                        title = stringResource(R.string.no_search_result),
                        description = stringResource(R.string.no_search_result_description),
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                    )
                }
            }
        }
    }
}

@Composable
private fun SuccessMediaItems(
    selectedTabOption: TabOption,
    moviesFlow: LazyPagingItems<MovieItemUiState>,
    tvShowsFlow: LazyPagingItems<TvShowItemUiState>,
    onPageLoading: (Boolean) -> Unit,
    onMovieClicked: (movieId: Long) -> Unit,
    onTvShowClicked: (tvShowId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedItems = if (selectedTabOption == TabOption.MOVIES) {
        moviesFlow
    } else {
        tvShowsFlow
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        modifier = modifier,
    ) {
        items(
            selectedItems.itemCount,
            key = { index -> getItemKey(selectedTabOption, index, selectedItems) },
        ) { index ->
            val mediaItem = selectedItems[index] ?: return@items
            when (mediaItem) {
                is MovieItemUiState -> {
                    MovieCard(
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = mediaItem.name,
                                model = mediaItem.posterImageUrl,
                                contentScale = ContentScale.Crop,
                                onLoading = { ImageLoadingIndicator() },
                                onError = { ImageErrorIndicator() },
                            )
                        },
                        movieType = stringResource(R.string.movies),
                        movieYear = mediaItem.yearOfRelease,
                        movieTitle = mediaItem.name,
                        movieRating = mediaItem.rate,
                        onClick = { onMovieClicked(mediaItem.id) }
                    )
                }

                is TvShowItemUiState -> {
                    MovieCard(
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = mediaItem.name,
                                model = mediaItem.posterImageUrl,
                                contentScale = ContentScale.Crop,
                                onLoading = { ImageLoadingIndicator() },
                                onError = { ImageErrorIndicator() },
                            )
                        },
                        movieType = stringResource(R.string.tv_shows),
                        movieYear = mediaItem.yearOfRelease,
                        movieTitle = mediaItem.name,
                        movieRating = mediaItem.rate,
                        onClick = {onTvShowClicked(mediaItem.id)}
                    )
                }
            }
        }

        selectedItems.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    onPageLoading(true)
                }

                is LoadState.NotLoading -> {
                    onPageLoading(false)
                }

                else -> onPageLoading(false)
            }
        }
    }
}

private fun getItemKey(
    selectedTabOption: TabOption,
    index: Int,
    selectedItems: LazyPagingItems<out Any>
): String {
    val item = selectedItems[index]
    val id = if (selectedTabOption == TabOption.MOVIES) (item as MovieItemUiState).id
             else (item as TvShowItemUiState).id
    return "${id}-${index}"
}

@Composable
private fun SearchScreenHeader(
    keyword: String,
    selectedTabOption: TabOption,
    onNavigateBackClicked: () -> Unit,
    onKeywordValuedChanged: (String) -> Unit,
    onFilterButtonClicked: () -> Unit,
    onTabOptionClicked: (TabOption) -> Unit,
    onSaveSearchHistory: () -> Unit,
    modifier: Modifier = Modifier,
    onHeaderSizeChanged: (IntSize) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.onSizeChanged(onSizeChanged = onHeaderSizeChanged),
    ) {
        DefaultAppBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(R.string.search),
            onNavigateBackClicked = onNavigateBackClicked,
        )
        TextField(
            modifier =
                Modifier
                    .background(color = AppTheme.color.surface)
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp),
            text = keyword,
            onValueChange = onKeywordValuedChanged,
            hintText = stringResource(R.string.search_hint),
            trailingIcon = com.amsterdam.ui.R.drawable.ic_filter_vertical,
            onTrailingClick = onFilterButtonClicked,
            isTrailingClickEnabled = keyword.isNotBlank(),
            isError = keyword.length > 100,
            errorMessage = stringResource(R.string.search_error_query_too_long),
            maxCharacters = 100,
            keyboardActions =
                KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        onKeywordValuedChanged(keyword)
                        onSaveSearchHistory()
                    },
                ),
            imeAction = ImeAction.Search,
        )
        AnimatedVisibility(keyword.isNotBlank()) {
            TabsLayout(
                modifier = Modifier.fillMaxWidth(),
                tabs =
                    listOf(
                        stringResource(R.string.movies),
                        stringResource(R.string.tv_shows),
                    ),
                selectedIndex = selectedTabOption.index,
                onSelectTab = { index -> onTabOptionClicked(TabOption.entries[index]) },
            )
        }
    }
}
