package com.example.ui.screens.topRated

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.R
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.theme.AppTheme
import com.example.ui.application.LocalNavController
import com.example.ui.components.NoNetworkContainer
import com.example.ui.components.appBar.DefaultAppBar
import com.example.ui.navigation.Route
import com.example.ui.screens.home.sections.AnimatedSectionVisibility
import com.example.ui.screens.topRated.component.TopRatedBackgroundComponent
import com.example.ui.screens.topRated.component.TopRatedMoviesGrid
import com.example.ui.utils.safeNavigate
import com.example.viewmodel.topRated.TopRatedEffect
import com.example.viewmodel.topRated.TopRatedInteractionListener
import com.example.viewmodel.topRated.TopRatedUiState
import com.example.viewmodel.topRated.TopRatedViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun TopRatedScreen(viewModel: TopRatedViewModel = koinViewModel()) {
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
                    LoadingContainer(modifier = Modifier.fillMaxSize().zIndex(10f))
            }

            AnimatedSectionVisibility(
                visible = state.error == TopRatedUiState.TopRatedError.NetworkError
            ) {
                    NoNetworkContainer(
                        onClickRetry = interactionListener::onClickRetryLoading
                    )
            }

            AnimatedSectionVisibility(
                visible = state.topRatedMovies.isNotEmpty()
            ) {
                TopRatedMoviesGrid(
                    gridState = gridState,
                    topRatedMovies = state.topRatedMovies,
                    onClickMovie = interactionListener::onClickMovie,
                    modifier = Modifier.weight(1f).navigationBarsPadding()
                )
            }
        }
    }
}
