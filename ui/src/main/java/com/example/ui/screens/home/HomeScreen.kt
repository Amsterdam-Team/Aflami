package com.example.ui.screens.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.entity.category.MovieGenre
import com.example.ui.application.LocalNavController
import com.example.ui.components.NoNetworkContainer
import com.example.ui.components.appBar.HomeAppBar
import com.example.ui.navigation.Route
import com.example.ui.navigation.Route.MovieDetails
import com.example.ui.screens.home.sections.AnimatedSectionVisibility
import com.example.ui.screens.home.sections.BlurredMoviePoster
import com.example.ui.screens.home.sections.topRatingSection
import com.example.ui.screens.home.sections.popularSection
import com.example.ui.screens.home.sections.upcomingMoviesSection
import com.example.ui.utils.safeNavigate
import com.example.viewmodel.home.HomeEffect
import com.example.viewmodel.home.HomeEffect.NavigateToMovieDetailsEffect
import com.example.viewmodel.home.HomeEffect.NavigateToSearchScreenEffect
import com.example.viewmodel.home.HomeInteractionListener
import com.example.viewmodel.home.HomeUiState
import com.example.viewmodel.home.HomeViewModel
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
                    NavigateToSearchScreenEffect -> navController.safeNavigate(Route.Search)
                    is NavigateToMovieDetailsEffect -> {
                        navController.safeNavigate(MovieDetails(movieId = effect.movieId))
                    }

                    HomeEffect.NavigateToTopRatedMoviesEffect -> {
                        navController.safeNavigate(Route.TopRated)
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
    val isSectionsVisible by remember(state.isLoading, state.error) {
        derivedStateOf { !state.isLoading && state.error == null }
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

    Box(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(nestedScrollConnection)) {
        AnimatedSectionVisibility(visible = state.popularMovies.isNotEmpty()) {
            BlurredMoviePoster(
                posterUrl = state.popularMovies[pagerState.currentPage % state.popularMovies.size].posterUrl,
                modifier = Modifier.offset { IntOffset(x = 0, y = blurOffsetY.roundToInt()) }
            )
        }
        AnimatedSectionVisibility(state.isLoading) {
            LoadingContainer(modifier = modifier.fillMaxSize())
        }
        AnimatedSectionVisibility(visible = state.error != null) {
            NoNetworkContainer(
                onClickRetry = interactionListener::onClickRetryLoading
            )
        }
        Column(modifier = Modifier.padding(bottom = 100.dp)) {
            HomeAppBar(
                modifier = Modifier
                    .background(appBarColor)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onSearchClicked = interactionListener::onClickSearch,
            )
            AnimatedSectionVisibility(visible = state.popularMovies.isNotEmpty()) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize(),
                    state = lazyListState,
                ) {

                    popularSection(
                        popularMovies = state.popularMovies,
                        pagerState = pagerState
                    )
                    topRatingSection(
                        topRatedMovies = state.topRatedMovies,
                        onClickMovie = interactionListener::onClickMovie,
                        onClickShowAll = interactionListener::onClickShowAllToRatedMovies
                    )

                    upcomingMoviesSection(
                        moviesGenres = state.upcomingMovieGenres,
                        onChangeMovieGenre = interactionListener::onChangeUpcomingMovieGenre,
                        movies = state.upcomingMovies,
                        onMovieClicked = interactionListener::onClickUpcomingMovieCard,
                        isVisible = isSectionsVisible
                    )
                }
            }
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
            }
        )
    }
}