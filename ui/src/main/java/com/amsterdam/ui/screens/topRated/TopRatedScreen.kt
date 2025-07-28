package com.amsterdam.ui.screens.topRated

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.ui.screens.topRated.component.TopRatedBackgroundComponent
import com.amsterdam.ui.screens.topRated.component.TopRatedMediaItemsGrid
import com.amsterdam.ui.utils.safeNavigate
import com.amsterdam.viewmodel.topRated.TopRatedEffect
import com.amsterdam.viewmodel.topRated.TopRatedInteractionListener
import com.amsterdam.viewmodel.topRated.TopRatedUiState
import com.amsterdam.viewmodel.topRated.TopRatedViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TopRatedScreen(viewModel: TopRatedViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    TopRatedContent(state, viewModel)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            it?.let {
                when (it) {
                    is TopRatedEffect.NavigateToMovieDetailsScreen -> {
                        navController.safeNavigate(Route.MovieDetails(it.movieId))
                    }

                    is TopRatedEffect.NavigateToTvShowDetailsEffect -> {
                        navController.safeNavigate(Route.SeriesDetails(it.tvShowId))
                    }

                    TopRatedEffect.NavigateBack -> {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

@Composable
private fun TopRatedContent(
    state: TopRatedUiState,
    interactionListener: TopRatedInteractionListener
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TopRatedBackgroundComponent()

        val gridState = rememberLazyGridState()

        val scrollOffset = remember {
            derivedStateOf { gridState.firstVisibleItemScrollOffset }
        }

        val appBarColor by animateColorAsState(
            targetValue = if (scrollOffset.value > 10) AppTheme.color.surface else Color.Transparent,
            animationSpec = tween(800),
            label = "AppBarScrollColor"
        )

        Column(modifier = Modifier.fillMaxSize()) {
            DefaultAppBar(
                title = stringResource(R.string.top_rating),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(appBarColor)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onNavigateBackClicked = interactionListener::onClickBack
            )

            AnimatedSectionVisibility(
                visible = state.isLoading
            ) {
                LoadingContainer(modifier = Modifier
                    .fillMaxSize()
                    .zIndex(10f))
            }

            AnimatedSectionVisibility(
                visible = state.error == TopRatedUiState.TopRatedError.NetworkError
            ) {
                NoNetworkContainer(
                    onClickRetry = interactionListener::onClickRetryLoading
                )
            }

            AnimatedSectionVisibility(
                visible = state.topRatedMediaItems.isNotEmpty()
            ) {
                TopRatedMediaItemsGrid(
                    gridState = gridState,
                    topRatedMediaItems = state.topRatedMediaItems,
                    onClickMediaItem = interactionListener::onClickMediaItem,
                    modifier = Modifier
                        .weight(1f)
                        .navigationBarsPadding()
                )
            }
        }
    }
}
