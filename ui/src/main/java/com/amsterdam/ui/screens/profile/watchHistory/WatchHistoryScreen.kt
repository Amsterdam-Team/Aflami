package com.amsterdam.ui.screens.profile.watchHistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.TabsLayout
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.ui.components.SuccessMediaItemsSection
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.watchHistory.WatchHistoryEffect
import com.amsterdam.viewmodel.watchHistory.WatchHistoryInteractionListener
import com.amsterdam.viewmodel.watchHistory.WatchHistoryUiState
import com.amsterdam.viewmodel.watchHistory.WatchHistoryViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WatchHistoryScreen(viewModel: WatchHistoryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    WatchHistoryContent(state, viewModel, viewModel::onClickTabOption)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is WatchHistoryEffect.NavigateToMovieDetails -> {
                    navController.navigate(Route.MovieDetails(effect.movieId))
                }

                is WatchHistoryEffect.NavigateToTvShowDetails -> {
                    navController.navigate(Route.SeriesDetails(effect.tvShowId))
                }

                WatchHistoryEffect.NavigateBack -> {
                    navController.navigateUp()
                }
            }
        }
    }
}

@Composable
fun WatchHistoryContent(
    state: WatchHistoryUiState,
    interactionListener: WatchHistoryInteractionListener,
    onTabOptionClicked: (TabOption) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        DefaultAppBar(
            title = stringResource(R.string.watch_history),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            onNavigateBackClicked = interactionListener::onClickBack
        )

        TabsLayout(
            modifier = Modifier.fillMaxWidth(),
            tabs =
                listOf(
                    stringResource(R.string.movies),
                    stringResource(R.string.tv_shows),
                ),
            selectedIndex = state.selectedTabOption.index,
            onSelectTab = { index -> onTabOptionClicked(TabOption.entries[index]) },
        )

        val selectedItems = if (state.selectedTabOption == TabOption.MOVIES) {
            state.movies.collectAsLazyPagingItems()
        } else {
            state.tvShows.collectAsLazyPagingItems()
        }

        AnimatedSectionVisibility(visible = selectedItems.itemCount > 0) {
            SuccessMediaItemsSection(
                onMovieClicked = interactionListener::onClickMovieCard,
                onTvShowClicked = interactionListener::onClickTvShowCard,
                selectedItems = selectedItems
            )
        }

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
            visible = state.error == WatchHistoryUiState.WatchHistoryError.NetworkError
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
    }
}