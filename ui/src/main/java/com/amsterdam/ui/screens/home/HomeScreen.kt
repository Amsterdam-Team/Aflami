package com.amsterdam.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.domain.useCase.mood.GetMoviesByMoodUseCase.Mood
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.application.LocalScaffoldBottomPadding
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.HomeAppBar
import com.amsterdam.ui.screens.home.component.MovieMoodPickerDialog
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.ui.screens.home.sections.MoodPickerSection
import com.amsterdam.ui.screens.home.sections.continueWatchingSection
import com.amsterdam.ui.screens.home.sections.popularSection
import com.amsterdam.ui.screens.home.sections.topRatingSection
import com.amsterdam.ui.screens.home.sections.upcomingMoviesSection
import com.amsterdam.viewmodel.home.HomeEffect
import com.amsterdam.viewmodel.home.HomeEffect.NavigateToMovieDetailsEffect
import com.amsterdam.viewmodel.home.HomeEffect.NavigateToSearchScreenEffect
import com.amsterdam.viewmodel.home.HomeInteractionListener
import com.amsterdam.viewmodel.home.HomeUiState
import com.amsterdam.viewmodel.home.HomeViewModel
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = hiltViewModel()) {
    val navigationManager = LocalNavManager.current
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    val errorGetMoviesByMoodMessage = stringResource(R.string.error_mood_movies_loading)
    LaunchedEffect(Unit) {
        homeViewModel.effect.collectLatest { effect ->
            when (effect) {
                is NavigateToSearchScreenEffect -> navigationManager.toSearchByKeyword()

                is NavigateToMovieDetailsEffect -> {
                    navigationManager.toMovieDetails(effect.movieId)
                }

                is HomeEffect.NavigateToTvShowDetailsEffect -> {
                    navigationManager.toSeriesDetails(effect.tvShowId)
                }

                is HomeEffect.NavigateToTopRatedMoviesEffect -> {
                    navigationManager.toTopRated()
                }

                is HomeEffect.NavigateToContinueWatchingMoviesScreen -> {
                    navigationManager.toContinueWatching()
                }

                HomeEffect.ShowGetMoviesByMoodErrorSnackBar -> SnackBarManager.showError(
                    message = errorGetMoviesByMoodMessage
                )
            }
        }
    }

    HomeScreenContent(
        modifier = modifier, state = state, interactionListener = homeViewModel
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun HomeScreenContent(
    state: HomeUiState, interactionListener: HomeInteractionListener, modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val parentLazyListState = rememberLazyListState()
    val childLazyListState = rememberLazyListState()
    var canChildScroll by remember { mutableStateOf(!parentLazyListState.canScrollForward) }
    var appBarHeight by remember { mutableStateOf(0.dp) }
    val bottomNavigationBarPadding = LocalScaffoldBottomPadding.current.value.dp
    val deviceWidth = configuration.screenWidthDp
    val deviceHeightDp = configuration.screenHeightDp.dp
    val upcomingMoviesSectionHeightDp = calculateUpcomingMoviesSectionHeightDp(
        deviceHeightDp, appBarHeight, bottomNavigationBarPadding
    )

    val scrollOffset by remember {
        derivedStateOf { parentLazyListState.firstVisibleItemScrollOffset }
    }

    LaunchedEffect(state.upcomingMoviesSectionUiState) {
        childLazyListState.scrollToItem(0)
        canChildScroll = !parentLazyListState.canScrollForward
    }

    LaunchedEffect(parentLazyListState.isScrollInProgress) {
        canChildScroll = !parentLazyListState.canScrollForward
    }

    LaunchedEffect(childLazyListState.isScrollInProgress, !parentLazyListState.canScrollForward) {
        canChildScroll = childLazyListState.canScrollBackward && !parentLazyListState.canScrollForward
    }

    val appBarColor by animateColorAsState(
        targetValue = if (scrollOffset > 8) AppTheme.color.surface else Color.Transparent,
        animationSpec = tween(800),
        label = "AppBarScrollColor"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets(bottom = LocalScaffoldBottomPadding.current))
    ) {
        if (state.error == HomeUiState.HomeError.NetworkError) {
            Column(Modifier.fillMaxSize()) {
                HomeAppBar(
                    onSearchClicked = interactionListener::onClickSearch,
                    modifier = Modifier
                        .background(appBarColor)
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp),
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    NoNetworkContainer(
                        onClickRetry = interactionListener::onClickRetryLoading,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                state = parentLazyListState,
            ) {
                popularSection(
                    state = state.popularMediaSectionUiState,
                    onClickMediaItem = interactionListener::onClickMediaItem,
                    isVisible = state.error == null
                )

                continueWatchingSection(
                    state = state.continueWatchingMediaSectionUiState,
                    isVisible = state.continueWatchingMediaSectionUiState.mediaItems.isNotEmpty(),
                    onClickMediaItem = interactionListener::onClickMediaItem,
                    onClickShowAll = interactionListener::onClickShowAllContinueWatchingMovies,
                )

                topRatingSection(
                    state = state.topRatedMediaSectionUiState,
                    onClickMediaItem = interactionListener::onClickMediaItem,
                    onClickShowAll = interactionListener::onClickShowAllToRatedMovies,
                    isVisible = state.error == null,
                )

                item {
                    AnimatedSectionVisibility(
                        visible = !state.isLoading && state.error == null,
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        MoodPickerSection(
                            state = state.moodPickerUiState,
                            interactionListener = interactionListener
                        )
                    }
                }

                item {
                    LazyColumn(
                        state = childLazyListState,
                        userScrollEnabled = canChildScroll,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = upcomingMoviesSectionHeightDp)
                            .animateContentSize(tween(500))
                    ) {
                        upcomingMoviesSection(
                            state = state.upcomingMoviesSectionUiState,
                            onChangeMovieGenre = interactionListener::onChangeUpcomingMovieGenre,
                            onMovieClicked = interactionListener::onClickUpcomingMovieCard,
                            isVisible = !state.isLoading && state.error == null,
                            deviceWidth = deviceWidth,
                        )
                    }
                }
            }
            AnimatedSectionVisibility(visible = state.moodPickerUiState.openMovieDialog) {
                MovieMoodPickerDialog(
                    movie = state.moodPickerUiState.selectedMovie,
                    onClickViewDetails = interactionListener::onClickViewDetails,
                    onClickGetAnotherMovie = interactionListener::onClickGetAnotherMovie,
                    onDismiss = interactionListener::onDismissMoodPickerDialog,
                    modifier = Modifier.wrapContentSize()
                )
            }
            HomeAppBar(
                onSearchClicked = interactionListener::onClickSearch,
                modifier = Modifier
                    .onSizeChanged {
                        appBarHeight = with(density) { it.height.toDp() }
                    }
                    .background(appBarColor)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp))
        }
    }
}

private fun calculateUpcomingMoviesSectionHeightDp(
    contentHeightDp: Dp, appBarHeight: Dp, bottomNavigationBarPadding: Dp
) = contentHeightDp - appBarHeight - bottomNavigationBarPadding

@ThemeAndLocalePreviews
@Composable
private fun HomeScreenPreview() {
    AflamiTheme {
        HomeScreenContent(
            state = HomeUiState(), interactionListener = object : HomeInteractionListener {
                override fun onClickRetryLoading() {}
                override fun onClickSearch() {}

                override fun onClickUpcomingMovieCard(id: Long) {}
                override fun onChangeUpcomingMovieGenre(genre: MovieGenre) {}
                override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {}
                override fun onClickShowAllToRatedMovies() {}
                override fun onClickShowAllContinueWatchingMovies() {}

                override fun onChangeMood(mood: Mood) {}
                override fun onClickGetNow() {}
                override fun onDismissMoodPickerDialog() {}
                override fun onClickViewDetails() {}
                override fun onClickGetAnotherMovie() {}
            })
    }
}