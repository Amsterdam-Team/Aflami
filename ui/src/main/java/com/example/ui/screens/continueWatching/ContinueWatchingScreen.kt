package com.example.ui.screens.continueWatching

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.R
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.theme.AppTheme
import com.example.ui.application.LocalNavController
import com.example.ui.components.NoNetworkContainer
import com.example.ui.components.appBar.DefaultAppBar
import com.example.ui.navigation.Route
import com.example.ui.screens.continueWatching.component.ContinueWatchingMoviesGrid
import com.example.ui.screens.home.sections.AnimatedSectionVisibility
import com.example.ui.utils.safeNavigate
import com.example.viewmodel.continueWatching.ContinueWatchingEffect
import com.example.viewmodel.continueWatching.ContinueWatchingInteractionListener
import com.example.viewmodel.continueWatching.ContinueWatchingUiState
import com.example.viewmodel.continueWatching.ContinueWatchingViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ContinueWatchingScreen(viewModel: ContinueWatchingViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    ContinueWatchingContent(state, viewModel)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            it?.let {
                when (it) {
                    is ContinueWatchingEffect.NavigateToMovieDetailsScreen -> {
                        navController.safeNavigate(Route.MovieDetails(it.movieId))
                    }

                    ContinueWatchingEffect.NavigateBack -> {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

@Composable
fun ContinueWatchingContent(
    state: ContinueWatchingUiState,
    interactionListener: ContinueWatchingInteractionListener
) {
    val gridState = rememberLazyGridState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
    ) {
        DefaultAppBar(
            title = stringResource(R.string.continue_watching),
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.color.surface)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onNavigateBackClicked = interactionListener::onClickBack
        )

        AnimatedSectionVisibility(
            visible = state.isLoading
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingContainer(modifier = Modifier.zIndex(10f))
            }
        }

        AnimatedSectionVisibility(
            visible = state.error == ContinueWatchingUiState.ContinueWatchingError.NetworkError
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                NoNetworkContainer(
                    onClickRetry = interactionListener::onClickRetryLoading
                )
            }
        }

        AnimatedSectionVisibility(
            visible = state.continueWatchingMovies.isNotEmpty()
        ) {
            ContinueWatchingMoviesGrid(
                gridState = gridState,
                continueWatchingMovies = state.continueWatchingMovies,
                onClickMovie = interactionListener::onClickMovie,
                modifier = Modifier.weight(1f)
            )
        }
    }
}