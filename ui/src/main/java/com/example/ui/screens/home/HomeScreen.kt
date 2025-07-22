package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.entity.category.MovieGenre
import com.example.ui.application.LocalNavController
import com.example.ui.components.appBar.HomeAppBar
import com.example.ui.navigation.Route
import com.example.ui.screens.home.sections.homeErrorContent
import com.example.ui.screens.home.sections.homeLoadingContent
import com.example.ui.screens.home.sections.popularSection
import com.example.ui.screens.home.sections.upcomingMoviesSection
import com.example.ui.utils.safeNavigate
import com.example.viewmodel.home.HomeEffect.NavigateToMovieDetailsEffect
import com.example.viewmodel.home.HomeEffect.NavigateToSearchScreenEffect
import com.example.viewmodel.home.HomeInteractionListener
import com.example.viewmodel.home.HomeUiState
import com.example.viewmodel.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

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
                        navController.safeNavigate(Route.MovieDetails(movieId = effect.movieId))
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
    //val isSectionsVisible by remember { mutableStateOf(!state.isLoading && state.error == null) }
    val isSectionsVisible = !state.isLoading && state.error == null

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {

        item {
            HomeAppBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                onSearchClicked = interactionListener::onClickSearch,
            )
        }

        popularSection(
            popularMovies = state.popularMovies,
            isVisible = isSectionsVisible && state.popularMovies.isNotEmpty()
        )

        upcomingMoviesSection(
            moviesGenres = state.upcomingMovieGenres,
            onChangeMovieGenre = interactionListener::onChangeUpcomingMovieGenre,
            movies = state.upcomingMovies,
            onMovieClicked = interactionListener::onClickUpcomingMovieCard,
            isVisible = isSectionsVisible && state.upcomingMovies.isNotEmpty()
        )

        homeErrorContent(
            error = state.error,
            onClickRetryLoading = interactionListener::onClickRetryLoading
        )

        homeLoadingContent(isLoading = state.isLoading)
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
            }
        )
    }
}