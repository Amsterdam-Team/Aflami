package com.example.ui.screens.search

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
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.NoDataContainer
import com.example.designsystem.components.NoNetworkContainer
import com.example.designsystem.components.TabsLayout
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AppTheme
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.application.LocalNavController
import com.example.ui.navigation.Route
import com.example.ui.screens.search.sections.RecentSearchesSection
import com.example.ui.screens.search.sections.SuggestionsHubSection
import com.example.ui.screens.search.sections.filterDialog.FilterDialog
import com.example.viewmodel.common.MediaItemUiState
import com.example.viewmodel.common.MediaType
import com.example.viewmodel.search.searchByKeyword.FilterInteractionListener
import com.example.viewmodel.search.searchByKeyword.SearchErrorState
import com.example.viewmodel.search.searchByKeyword.SearchInteractionListener
import com.example.viewmodel.search.searchByKeyword.SearchUiEffect
import com.example.viewmodel.search.searchByKeyword.SearchUiState
import com.example.viewmodel.search.searchByKeyword.SearchViewModel
import com.example.viewmodel.search.searchByKeyword.TabOption
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SearchScreen(viewModel: SearchViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SearchUiEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                SearchUiEffect.NavigateToActorSearch -> {
                    navController.navigate(Route.SearchByActor)
                }

                SearchUiEffect.NavigateToMovieDetails -> {}
                SearchUiEffect.NavigateToWorldSearch -> {
                    navController.navigate(Route.SearchByCountry)
                }

                null -> {}
            }
        }
    }

    SearchContent(state = state, interaction = viewModel, filterInteraction = viewModel)
}

@Composable
private fun SearchContent(
    state: SearchUiState,
    interaction: SearchInteractionListener,
    filterInteraction: FilterInteractionListener,
) {
    BackHandler(enabled = state.keyword.isNotEmpty()) {
        interaction.onSearchCleared()
    }
    var headerHeight by remember { mutableStateOf(0.dp) }

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
            onNavigateBackClicked = interaction::onNavigateBackClicked,
            onKeywordValuedChanged = interaction::onKeywordValuedChanged,
            onFilterButtonClicked = interaction::onFilterButtonClicked,
            onSearchActionClicked = interaction::onSearchActionClicked,
            onTabOptionClicked = interaction::onTabOptionClicked,
            onHeaderSizeChanged = {
                headerHeight = it.height.dp
            },
        )

        AnimatedVisibility(state.isLoading && state.errorUiState == null) {
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
                onCancelButtonClicked = filterInteraction::onCancelButtonClicked,
                onRatingStarChanged = filterInteraction::onRatingStarChanged,
                onMovieGenreButtonChanged = filterInteraction::onMovieGenreButtonChanged,
                onTvGenreButtonChanged = filterInteraction::onTvGenreButtonChanged,
                onApplyButtonClicked = filterInteraction::onApplyButtonClicked,
                onClearButtonClicked = filterInteraction::onClearButtonClicked,
            )
        }

        AnimatedVisibility(state.keyword.isNotBlank() && state.errorUiState == null) {
            SuccessMediaItems(
                movies = state.movies,
                tvShows = state.tvShows,
                selectedTabOption = state.selectedTabOption,
            )
        }

        SuggestionsHubSection(
            keyword = state.keyword,
            onWorldSearchCardClicked = interaction::onWorldSearchCardClicked,
            onActorSearchCardClicked = interaction::onActorSearchCardClicked,
        )

        RecentSearchesSection(
            keyword = state.keyword,
            recentSearches = state.recentSearches,
            onAllRecentSearchesCleared = interaction::onAllRecentSearchesCleared,
            onRecentSearchClicked = interaction::onRecentSearchClicked,
            onRecentSearchCleared = interaction::onRecentSearchCleared,
        )

        CenterOfScreenContainer(
            unneededSpace = headerHeight,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp),
        ) {
            AnimatedVisibility(state.keyword.isNotBlank() && state.errorUiState != null) {
                if (state.errorUiState == SearchErrorState.NoNetworkConnection) {
                    NoNetworkContainer(
                        onClickRetry = interaction::onRetryQuestClicked,
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                    )
                }

                if (state.errorUiState == SearchErrorState.NoResultFoundException) {
                    NoDataContainer(
                        imageRes = painterResource(R.drawable.placeholder_no_result_found),
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
    movies: List<MediaItemUiState>,
    tvShows: List<MediaItemUiState>,
    selectedTabOption: TabOption,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        modifier = modifier,
    ) {
        items(
            if (selectedTabOption == TabOption.MOVIES) {
                movies
            } else {
                tvShows
            },
        ) { mediaItem ->
            with(mediaItem) {
                MovieCard(
                    movieImage = {
                        SafeImageView(
                            modifier =
                                Modifier
                                    .fillMaxSize(),
                            contentDescription = name,
                            model = posterImage,
                            contentScale = ContentScale.Crop,
                        )
                    },
                    movieType =
                        if (mediaType == MediaType.TV_SHOW) {
                            stringResource(R.string.tv_shows)
                        } else {
                            stringResource(R.string.movies)
                        },
                    movieYear = yearOfRelease,
                    movieTitle = name,
                    movieRating = rate,
                )
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
            trailingIcon = R.drawable.ic_filter_vertical,
            onTrailingClick = onFilterButtonClicked,
            isTrailingClickEnabled = keyword.isNotBlank(),
            isError = keyword.length > 100,
            errorMessage = stringResource(R.string.search_error_query_too_long),
            maxCharacters = 100,
            keyboardActions =
                KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        onSearchActionClicked()
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
