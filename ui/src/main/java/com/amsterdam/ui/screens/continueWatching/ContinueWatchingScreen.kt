package com.amsterdam.ui.screens.continueWatching

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.continueWatching.component.ContinueWatchingMediaItemsGrid
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingEffect
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingInteractionListener
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingUiState
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ContinueWatchingScreen(viewModel: ContinueWatchingViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    ContinueWatchingContent(state, viewModel)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ContinueWatchingEffect.NavigateToMovieDetailsScreen -> {
                    navController.navigate(Route.MovieDetails(effect.movieId))
                }

                is ContinueWatchingEffect.NavigateToTvShowDetailsEffect -> {
                    navController.navigate(Route.SeriesDetails(effect.tvShowId))
                }

                ContinueWatchingEffect.NavigateBack -> {
                    navController.navigateUp()
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
            visible = state.continueMediaItemUiStates.isNotEmpty()
        ) {
            ContinueWatchingMediaItemsGrid(
                continueWatchingMediaItems = state.continueMediaItemUiStates,
                onClickMediaItem = interactionListener::onClickMediaItem,
                modifier = Modifier.weight(1f),
                gridState = gridState
            )
        }
    }
}