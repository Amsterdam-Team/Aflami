package com.example.ui.screens.search.keywordSearch

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
import androidx.compose.foundation.lazy.grid.items
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
import com.example.designsystem.R
import com.example.designsystem.components.CenterOfScreenContainer
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.components.TabsLayout
import com.example.designsystem.components.TextField
import com.example.designsystem.theme.AppTheme
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.application.LocalNavController
import com.example.ui.components.MovieCard
import com.example.ui.components.NoDataContainer
import com.example.ui.components.NoNetworkContainer
import com.example.ui.components.appBar.DefaultAppBar
import com.example.ui.navigation.Route
import com.example.ui.navigation.Route.MovieDetails
import com.example.ui.navigation.Route.SeriesDetails
import com.example.ui.screens.search.keywordSearch.sections.RecentSearchesSection
import com.example.ui.screens.search.keywordSearch.sections.SuggestionsHubSection
import com.example.ui.screens.search.keywordSearch.sections.filterDialog.FilterDialog
import com.example.viewmodel.search.keywordSearch.FilterInteractionListener
import com.example.viewmodel.search.keywordSearch.SearchErrorState
import com.example.viewmodel.search.keywordSearch.SearchInteractionListener
import com.example.viewmodel.search.keywordSearch.SearchUiEffect
import com.example.viewmodel.search.keywordSearch.SearchUiState
import com.example.viewmodel.search.keywordSearch.SearchViewModel
import com.example.viewmodel.search.keywordSearch.TabOption
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import com.example.viewmodel.shared.uiStates.TvShowItemUiState
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            effect?.let {
                when (effect) {
                    SearchUiEffect.NavigateBack -> navController.popBackStack()
                    SearchUiEffect.NavigateToActorSearch -> navController.navigate(Route.SearchByActor)
                    is SearchUiEffect.NavigateToMovieDetails ->
                        navController.navigate(MovieDetails(effect.movieId))
                    SearchUiEffect.NavigateToWorldSearch -> navController.navigate(Route.SearchByCountry)
                    is SearchUiEffect.NavigateToTvShowDetails ->
                        navController.navigate(SeriesDetails(effect.tvShowId))
                }
            }
        }
    }

    SearchContent(state = state, interaction = viewModel, filterInteraction = viewModel)
}

@Composable
private fun SearchContent(
    state: SearchUiState,
    interaction: SearchInteractionListener,
    filterInteraction: FilterInteractionListener
) {
    BackHandler(enabled = state.keyword.isNotEmpty()) {
        interaction.onClickClearSearch()
    }
    var headerHeight by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        SearchScreenHeader(
            keyword = state.keyword,
            selectedTabOption = state.selectedTabOption,
            onNavigateBackClicked = interaction::onClickNavigateBack,
            onKeywordValuedChanged = interaction::onChangeSearchKeyword,
            onFilterButtonClicked = interaction::onClickFilterButton,
            onSearchActionClicked = interaction::onClickSearchAction,
            onTabOptionClicked = interaction::onClickTabOption,
            onHeaderSizeChanged = {
                headerHeight = it.height.dp
            }
        )

        AnimatedVisibility(state.isLoading && state.errorUiState == null) {
            CenterOfScreenContainer(unneededSpace = headerHeight) {
                LoadingContainer()
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            visible = state.isDialogVisible
        ) {
            FilterDialog(
                filterState = state.filterItemUiState,
                selectedTabOption = state.selectedTabOption,
                onCancelButtonClicked = filterInteraction::onClickCancel,
                onRatingStarChanged = filterInteraction::onChangeRatingStar,
                onMovieGenreButtonChanged = filterInteraction::onChangeMovieGenre,
                onTvGenreButtonChanged = filterInteraction::onChangeTvShowGenre,
                onApplyButtonClicked = filterInteraction::onClickApply,
                onClearButtonClicked = filterInteraction::onClickClear
            )
        }

        AnimatedVisibility(state.keyword.isNotBlank() && state.errorUiState == null) {
            SuccessMediaItems(
                onMovieClicked = interaction::onClickMovieCard,
                onTvShowClicked = interaction::onClickTvShowCard,
                tvShows = state.tvShows,
                movies = state.movies,
                selectedTabOption = state.selectedTabOption
            )
        }

        SuggestionsHubSection(
            keyword = state.keyword,
            onWorldSearchCardClicked = interaction::onClickWorldSearchCard,
            onActorSearchCardClicked = interaction::onClickActorSearchCard
        )

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
            AnimatedVisibility(state.keyword.isNotBlank() && state.errorUiState != null) {
                if (state.errorUiState is SearchErrorState.NoNetworkConnection) {
                    NoNetworkContainer(
                        onClickRetry = interaction::onClickRetryRequest,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }

                val isSelectedTabSearchResultEmpty =
                    if (state.selectedTabOption == TabOption.MOVIES) {
                        state.movies.isEmpty()
                    } else {
                        state.tvShows.isEmpty()
                    }

                AnimatedVisibility(isSelectedTabSearchResultEmpty) {
                    NoDataContainer(
                        imageRes = painterResource(com.example.ui.R.drawable.placeholder_no_result_found),
                        title = stringResource(R.string.no_search_result),
                        description = stringResource(R.string.no_search_result_description),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            }
        }
    }
}

@Composable
private fun SuccessMediaItems(
    movies: List<MovieItemUiState>,
    tvShows: List<TvShowItemUiState>,
    selectedTabOption: TabOption,
    modifier: Modifier = Modifier,
    onMovieClicked: (movieId: Long) -> Unit,
    onTvShowClicked: (tvShowId: Long) -> Unit
) {
    val selectedItems = if (selectedTabOption == TabOption.MOVIES) {
        movies
    } else {
        tvShows
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        modifier = modifier
    ) {
        items(selectedItems) { mediaItem ->
            when (mediaItem) {
                is MovieItemUiState -> {
                    MovieCard(
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = mediaItem.name,
                                model = mediaItem.posterImageUrl,
                                contentScale = ContentScale.Crop,
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
    }
}

@Composable
private fun SearchScreenHeader(
    keyword: String,
    selectedTabOption: TabOption,
    onNavigateBackClicked: () -> Unit,
    onKeywordValuedChanged: (String) -> Unit,
    onFilterButtonClicked: () -> Unit,
    onSearchActionClicked: () -> Unit,
    onTabOptionClicked: (TabOption) -> Unit,
    modifier: Modifier = Modifier,
    onHeaderSizeChanged: (IntSize) -> Unit = {}
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.onSizeChanged(onSizeChanged = onHeaderSizeChanged)
    ) {
        DefaultAppBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(R.string.search),
            onNavigateBackClicked = onNavigateBackClicked
        )
        TextField(
            modifier = Modifier
                .background(color = AppTheme.color.surface)
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
            text = keyword,
            onValueChange = onKeywordValuedChanged,
            hintText = stringResource(R.string.search_hint),
            trailingIcon = com.example.ui.R.drawable.ic_filter_vertical,
            onTrailingClick = onFilterButtonClicked,
            isTrailingClickEnabled = keyword.isNotBlank(),
            isError = keyword.length > 100,
            errorMessage = stringResource(R.string.search_error_query_too_long),
            maxCharacters = 100,
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearchActionClicked()
                }
            ),
            imeAction = ImeAction.Search,
        )
        AnimatedVisibility(keyword.isNotBlank()) {
            TabsLayout(
                modifier = Modifier.fillMaxWidth(),
                tabs = listOf(
                    stringResource(R.string.movies),
                    stringResource(R.string.tv_shows)
                ),
                selectedIndex = selectedTabOption.index,
                onSelectTab = { index -> onTabOptionClicked(TabOption.entries[index]) },
            )
        }
    }
}