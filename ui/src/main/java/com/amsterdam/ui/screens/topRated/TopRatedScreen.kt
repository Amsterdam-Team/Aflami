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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.ui.screens.topRated.component.TopRatedBackgroundComponent
import com.amsterdam.ui.screens.topRated.component.TopRatedMediaItemsGrid
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.topRated.TopRatedEffect
import com.amsterdam.viewmodel.topRated.TopRatedInteractionListener
import com.amsterdam.viewmodel.topRated.TopRatedUiState
import com.amsterdam.viewmodel.topRated.TopRatedViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TopRatedScreen(viewModel: TopRatedViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val mediaItems = state.mediaItems.collectAsLazyPagingItems()
    TopRatedContent(state, viewModel, mediaItems)
    LaunchedEffect(mediaItems.loadState) {
        viewModel.onPagingLoadStateChanged(mediaItems.loadState)
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is TopRatedEffect.NavigateToMovieDetailsScreen -> {
                    navController.navigate(Route.MovieDetails(it.movieId))
                }

                is TopRatedEffect.NavigateToTvShowDetailsEffect -> {
                    navController.navigate(Route.SeriesDetails(it.tvShowId))
                }

                TopRatedEffect.NavigateBack -> {
                    navController.navigateUp()
                }
            }
        }
    }
}

@Composable
private fun TopRatedContent(
    state: TopRatedUiState,
    interactionListener: TopRatedInteractionListener,
    mediaItems: LazyPagingItems<MediaItemUiState>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        var headerHeight by remember { mutableStateOf(0.dp) }
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

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DefaultAppBar(
                title = stringResource(R.string.top_rating),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(appBarColor)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .onSizeChanged { headerHeight = it.height.dp },
                onNavigateBackClicked = interactionListener::onClickBack
            )

            AnimatedSectionVisibility(
                visible = state.isLoading
            ) {
                LoadingContainer(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(10f)
                )
            }

            AnimatedSectionVisibility(
                visible = state.error == TopRatedUiState.TopRatedError.NetworkError
            ) {
                CenterOfScreenContainer(
                    unneededSpace = headerHeight,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                        .verticalScroll(rememberScrollState())

                ) {
                    NoNetworkContainer(onClickRetry = interactionListener::onClickRetryLoading)
                }
            }

            AnimatedSectionVisibility(
                visible = mediaItems.itemCount > 0
            ) {
                TopRatedMediaItemsGrid(
                    gridState = gridState,
                    mediaItems = mediaItems,
                    onClickMediaItem = interactionListener::onClickMediaItem,
                    modifier = Modifier
                        .weight(1f)
                        .navigationBarsPadding()
                )
            }
        }
    }
}
