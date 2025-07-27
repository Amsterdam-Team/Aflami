package com.amsterdam.ui.screens.home

import com.amsterdam.ui.screens.home.sections.continueWatchingSection
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.domain.models.Mood
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.HomeAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.navigation.Route.MovieDetails
import com.amsterdam.ui.screens.home.component.MovieMoodPickerDialogDialog
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.ui.screens.home.sections.BlurredMoviePoster
import com.amsterdam.ui.screens.home.sections.MoodPickerSection
import com.amsterdam.ui.screens.home.sections.popularSection
import com.amsterdam.ui.screens.home.sections.upcomingMoviesSection
import com.amsterdam.ui.utils.safeNavigate
import com.amsterdam.viewmodel.home.HomeEffect
import com.amsterdam.viewmodel.home.HomeEffect.NavigateToMovieDetailsEffect
import com.amsterdam.viewmodel.home.HomeEffect.NavigateToSearchScreenEffect
import com.amsterdam.viewmodel.home.HomeInteractionListener
import com.amsterdam.viewmodel.home.HomeUiState
import com.amsterdam.viewmodel.home.HomeViewModel
import com.example.ui.screens.home.sections.topRatingSection
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val state by homeViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        homeViewModel.effect.collectLatest { effect ->
            effect?.let {
                when (effect) {
                    is NavigateToSearchScreenEffect -> navController.safeNavigate(Route.Search)
                    is NavigateToMovieDetailsEffect -> {
                        navController.safeNavigate(MovieDetails(movieId = effect.movieId))
                    }

                    is HomeEffect.NavigateToTopRatedMoviesEffect -> {
                        navController.safeNavigate(Route.TopRated)
                    }

                    is HomeEffect.NavigateToContinueWatchingMoviesScreen -> {
                        navController.safeNavigate(Route.ContinueWatching)
                    }
                }
            }
        }
    }

    HomeScreenContent(
        modifier = modifier,
        state = state,
        interactionListener = homeViewModel
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeUiState,
    interactionListener: HomeInteractionListener,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) { Int.MAX_VALUE }

    val scrollOffset = remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset }
    }

    val appBarColor by animateColorAsState(
        targetValue = if (scrollOffset.value > 10) AppTheme.color.surface else Color.Transparent,
        animationSpec = tween(800),
        label = "AppBarScrollColor"
    )
    var blurOffsetY by remember { mutableStateOf(-12f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val newOffset = blurOffsetY + available.y
                blurOffsetY = newOffset.coerceIn(minimumValue = -1000f, maximumValue = -12f)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        AnimatedSectionVisibility(
            visible = state.popularMoviesSectionUiState.movies.isNotEmpty() && remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }.value == 0
        ) {
            BlurredMoviePoster(
                posterUrl = state.popularMoviesSectionUiState.movies[pagerState.currentPage % state.popularMoviesSectionUiState.movies.size].posterUrl,
                modifier = Modifier.offset { IntOffset(x = 0, y = blurOffsetY.roundToInt()) }
            )
        }

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            state = lazyListState,
        ) {
            stickyHeader {
                HomeAppBar(
                    modifier = Modifier
                        .background(appBarColor)
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onSearchClicked = interactionListener::onClickSearch,
                )
            }

            popularSection(
                state = state.popularMoviesSectionUiState,
                pagerState = pagerState,
                isVisible = state.error == null,
                onClickMovie = interactionListener::onClickMovie
            )

            continueWatchingSection(
                state = state.continueWatchingMoviesSectionUiState,
                onClickMovie = interactionListener::onClickMovie,
                onClickShowAll = interactionListener::onClickShowAllContinueWatchingMovies,
                isVisible = state.continueWatchingMoviesSectionUiState.movies.isNotEmpty(),
            )

            topRatingSection(
                state = state.topRatedMoviesSectionUiState,
                onClickMovie = interactionListener::onClickMovie,
                onClickShowAll = interactionListener::onClickShowAllToRatedMovies,
                isVisible = state.error == null
            )

            item { MoodPickerSection(state, interactionListener) }

            upcomingMoviesSection(
                state = state.upcomingMoviesSectionUiState,
                onChangeMovieGenre = interactionListener::onChangeUpcomingMovieGenre,
                onMovieClicked = interactionListener::onClickUpcomingMovieCard,
                isVisible = state.error == null
            )

            item {
                AnimatedSectionVisibility(
                    visible = state.error != null,
                    modifier = Modifier.fillParentMaxWidth().padding(top = 16.dp)
                ) {
                    NoNetworkContainer(onClickRetry = interactionListener::onClickRetryLoading, description = "")
                }
            }
        }

        AnimatedSectionVisibility(visible = state.moodPickerUiState.openMovieDialog) {
            MovieMoodPickerDialogDialog(
                movie = state.moodPickerUiState.selectedMovie,
                onClickViewDetails = interactionListener::onClickViewDetails,
                onClickGetAnotherMovie = interactionListener::onClickGetAnotherMovie,
                onDismiss = interactionListener::onDismissMoodPickerDialog,
                modifier = Modifier.fillMaxSize()
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
                override fun onClickMovie(movieId: Long) {}
                override fun onClickShowAllToRatedMovies() {}
                override fun onClickShowAllContinueWatchingMovies() {}

                override fun onClickMood(mood: Mood) {}
                override fun onClickGetNow() {}
                override fun onDismissMoodPickerDialog() {}
                override fun onClickViewDetails() {}
                override fun onClickGetAnotherMovie() {}
            }
        )
    }
}