package com.amsterdam.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.domain.models.Mood
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.application.LocalScaffoldBottomPadding
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.HomeAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.navigation.Route.*
import com.amsterdam.ui.navigation.Route.MovieDetails
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
    val navController = LocalNavController.current
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    val errorGetMoviesByMoodMessage = stringResource(R.string.error_mood_movies_loading)
    LaunchedEffect(Unit) {
        homeViewModel.effect.collectLatest { effect ->
            when (effect) {
                is NavigateToSearchScreenEffect -> navController.navigate(Search)

                is NavigateToMovieDetailsEffect -> {
                    navController.navigate(MovieDetails(movieId = effect.movieId))
                }

                is HomeEffect.NavigateToTvShowDetailsEffect -> {
                    navController.navigate(SeriesDetails(tvShowId = effect.tvShowId))
                }

                is HomeEffect.NavigateToTopRatedMoviesEffect -> {
                    navController.navigate(TopRated)
                }

                is HomeEffect.NavigateToContinueWatchingMoviesScreen -> {
                    navController.navigate(ContinueWatching)
                }

                HomeEffect.ShowGetMoviesByMoodErrorSnackBar -> SnackBarManager.showError(
                    message = errorGetMoviesByMoodMessage
                )
            }
        }
    }

    HomeScreenContent(
        modifier = modifier,
        state = state,
        interactionListener = homeViewModel
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun HomeScreenContent(
    state: HomeUiState,
    interactionListener: HomeInteractionListener,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    var upcomingMoviesSectionYOffsetDp by remember { mutableStateOf(0.dp) }
    val deviceWidth = configuration.screenWidthDp

    val scrollOffset = remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset }
    }
    val appBarColor by animateColorAsState(
        targetValue = if (scrollOffset.value > 8) AppTheme.color.surface else Color.Transparent,
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
                contentPadding = PaddingValues(bottom = 100.dp),
                state = lazyListState,
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
                    AnimatedSectionVisibility(visible = !state.isLoading && state.error == null) {
                        MoodPickerSection(
                            state = state.moodPickerUiState,
                            interactionListener = interactionListener
                        )
                    }
                }

                upcomingMoviesSection(
                    state = state.upcomingMoviesSectionUiState,
                    onChangeMovieGenre = interactionListener::onChangeUpcomingMovieGenre,
                    onMovieClicked = interactionListener::onClickUpcomingMovieCard,
                    isVisible = !state.isLoading && state.error == null,
                    onVerticalOffsetChange = {
                        upcomingMoviesSectionYOffsetDp = it
                    },
                    deviceWidth = deviceWidth,
                )

                if (state.error == null) {
                    if (!state.upcomingMoviesSectionUiState.isLoading) {
                        item {
                            val lastVisibleItemInfo by remember { derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull() } }
                            val totalItemsCount by remember { derivedStateOf { lazyListState.layoutInfo.totalItemsCount } }


                            val spacerHeight: Dp by remember {
                                derivedStateOf {
                                    if (upcomingMoviesSectionYOffsetDp > 0.dp || (totalItemsCount > 0 && lastVisibleItemInfo?.index == totalItemsCount - 1)) {
                                        screenHeightDp
                                    } else {
                                        0.dp
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(spacerHeight))
                        }
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
                    .background(appBarColor)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}


@ThemeAndLocalePreviews
@Composable
private fun HomeScreenPreview() {
    AflamiTheme {
        HomeScreenContent(
            state = HomeUiState(),
            interactionListener = object : HomeInteractionListener {
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
            }
        )
    }
}