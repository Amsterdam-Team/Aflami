package com.amsterdam.ui.screens.myRating

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.TabsLayout
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route.MovieDetails
import com.amsterdam.ui.navigation.Route.SeriesDetails
import com.amsterdam.ui.screens.myRating.placeholders.emptyRatingListPlaceholder
import com.amsterdam.ui.screens.myRating.placeholders.mediaCardsPlaceholder
import com.amsterdam.ui.utils.SavedStateKeys
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.myRating.MyRatingInteractionListener
import com.amsterdam.viewmodel.myRating.MyRatingUiEffect
import com.amsterdam.viewmodel.myRating.MyRatingUiState
import com.amsterdam.viewmodel.myRating.MyRatingUiState.MyRatingErrorState
import com.amsterdam.viewmodel.myRating.MyRatingViewModel
import com.amsterdam.viewmodel.shared.TabOption
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyRatingScreen(
    viewModel: MyRatingViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsState()

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val savedStateHandle = currentBackStackEntry?.savedStateHandle

    val refreshAfterRating = savedStateHandle
        ?.getStateFlow(SavedStateKeys.REFRESH_AFTER_RATING, false)
        ?.collectAsState()

    LaunchedEffect(refreshAfterRating?.value) {
        if (refreshAfterRating?.value == true) {
            viewModel.refreshRatedContent()
            savedStateHandle[SavedStateKeys.REFRESH_AFTER_RATING] = false
        }
    }

    val successRateDeletionMessage = stringResource(R.string.your_rate_deletion_has_been_saved)
    val errorRateDeletionMessage = stringResource(R.string.failed_to_delete_your_rating)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            with(navController) {
                when (effect) {
                    is MyRatingUiEffect.NavigateBack -> navigateUp()
                    is MyRatingUiEffect.NavigateToMovieDetails -> navigate(MovieDetails(movieId = effect.movieId))
                    is MyRatingUiEffect.NavigateToSeriesDetails -> navigate(SeriesDetails(tvShowId = effect.tvShowId))
                    MyRatingUiEffect.ShowDeleteRateSuccessSnackBar -> SnackBarManager.showSuccess(
                        message = successRateDeletionMessage
                    )

                    MyRatingUiEffect.ShowDeleteRateErrorSnackBar -> SnackBarManager.showError(
                        message = errorRateDeletionMessage
                    )
                }
            }
        }
    }

    MyRatingContent(state = state, interaction = viewModel)
}

@Composable
private fun MyRatingContent(
    state: MyRatingUiState,
    interaction: MyRatingInteractionListener
) {
    val lazyState = rememberLazyGridState()

    var appBarHeight by remember { mutableIntStateOf(0) }
    var tabsHeight by remember { mutableIntStateOf(0) }
    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
    val availableHeight =
        calculateAvailableHeight(appBarHeightPx = appBarHeight, tabsHeightPx = tabsHeight)

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .statusBarsPadding()
            .systemBarsPadding()
            .animateContentSize(),
        state = lazyState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 88.dp, start = 16.dp, end = 16.dp),
        columns = GridCells.Adaptive(160.dp)
    ) {
        item {
            DefaultAppBar(
                modifier = Modifier.onSizeChanged { appBarHeight = it.height },
                title = stringResource(R.string.my_rating),
                onNavigateBackClicked = interaction::onClickNavigateBack,
            )
        }

        stickyHeader {
            TabsLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { tabsHeight = it.height },
                tabs = listOf(stringResource(R.string.movies), stringResource(R.string.tv_shows)),
                selectedIndex = state.selectedTabOption.index,
                onSelectTab = { index -> interaction.onChangeTabOption(TabOption.entries[index]) },
            )
        }

        val isMovieTabSelected = state.selectedTabOption.index == TabOption.MOVIES.index
        val isTvShowTabSelected = state.selectedTabOption.index == TabOption.TV_SHOWS.index

        when {
            state.isLoading && !state.isRetryLoading -> mediaCardsPlaceholder()

            state.movies.isNotEmpty() && isMovieTabSelected -> {
                items(state.movies, key = { it.id }) { movie ->
                    with(movie) {
                        MediaCard(
                            modifier = Modifier.animateItem(),
                            movieImage = {
                                SafeImageView(
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = name,
                                    model = posterImageUrl,
                                    safetyLevel = safetyLevel,
                                    onLoading = { ImageLoadingIndicator() },
                                    onError = { ImageErrorIndicator() },
                                )
                            },
                            movieType = stringResource(R.string.movies),
                            movieYear = yearOfRelease,
                            movieTitle = name,
                            movieRating = rate,
                            topIcon = painterResource(R.drawable.ic_star_off),
                            onTopIconClick = { interaction.onClickDeleteMyMovieRatingIcon(id) },
                            onClick = { interaction.onClickMovieCard(id) }
                        )
                    }
                }
            }

            state.tvShows.isNotEmpty() && isTvShowTabSelected -> {
                items(state.tvShows, key = { it.id }) { movie ->
                    with(movie) {
                        MediaCard(
                            modifier = Modifier.animateItem(),
                            movieImage = {
                                SafeImageView(
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = name,
                                    safetyLevel = safetyLevel,
                                    model = posterImageUrl,
                                    onLoading = { ImageLoadingIndicator() },
                                    onError = { ImageErrorIndicator() },
                                )
                            },
                            movieType = stringResource(R.string.movies),
                            movieYear = yearOfRelease,
                            movieTitle = name,
                            movieRating = rate,
                            topIcon = painterResource(R.drawable.ic_star_off),
                            onTopIconClick = { interaction.onClickDeleteMyTvShowRatingIcon(id) },
                            onClick = { interaction.onClickTvShowCard(id) }
                        )
                    }
                }
            }

            state.error != null -> {
                getErrorContentByErrorUiState(
                    modifier = Modifier.height(availableHeight),
                    errorState = state.error ?: MyRatingErrorState.UnknownError,
                    interaction = interaction,
                    showRetryLoading = state.isRetryLoading

                )
            }

            state.movies.isEmpty() && isMovieTabSelected && !state.isLoading -> {
                emptyRatingListPlaceholder(
                    modifier = Modifier.height(availableHeight),
                    titleRes = R.string.no_rated_movies,
                    descriptionRes = R.string.no_rated_movies_description,
                )
            }

            state.tvShows.isEmpty() && isTvShowTabSelected && !state.isLoading -> {
                emptyRatingListPlaceholder(
                    modifier = Modifier.height(availableHeight),
                    titleRes = R.string.no_rated_tv_shows,
                    descriptionRes = R.string.no_rated_tv_shows_description,
                )
            }
        }
    }
}

@Composable
private fun calculateAvailableHeight(
    appBarHeightPx: Int,
    tabsHeightPx: Int,
    verticalContentPadding: Dp = 40.dp
): Dp {
    val density = LocalDensity.current
    val screenHeightPx = LocalWindowInfo.current.containerSize.height
    val screenHeightDp = with(density) { screenHeightPx.toDp() }

    val headerHeightDp = with(density) { (appBarHeightPx + tabsHeightPx).toDp() }

    val topSystemBarPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
    val bottomSystemBarPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    return screenHeightDp - headerHeightDp - topSystemBarPadding - bottomSystemBarPadding - verticalContentPadding
}

private fun LazyGridScope.getErrorContentByErrorUiState(
    errorState: MyRatingErrorState,
    interaction: MyRatingInteractionListener,
    modifier: Modifier = Modifier,
    showRetryLoading: Boolean
) {
    return when (errorState) {
        MyRatingErrorState.NoInternetError -> item(span = { GridItemSpan(maxLineSpan) }) {
            NoNetworkContainer(
                modifier = modifier,
                onClickRetry = interaction::onClickRetryRequest,
                showRetryLoading = showRetryLoading
            )
        }

        else -> emptyRatingListPlaceholder(
            modifier = modifier,
            titleRes = R.string.error_rated_media_title,
            descriptionRes = R.string.error_rated_media_description,
        )
    }
}
