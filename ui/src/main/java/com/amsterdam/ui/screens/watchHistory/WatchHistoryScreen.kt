package com.amsterdam.ui.screens.watchHistory

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.TabsLayout
import com.amsterdam.ui.R.drawable.no_saved_items
import com.amsterdam.ui.R.string.no_items_here
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.components.grids.SuccessMovieMediaItemsSection
import com.amsterdam.ui.components.grids.SuccessTvShowMediaItemsSection
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.watchHistory.WatchHistoryEffect
import com.amsterdam.viewmodel.watchHistory.WatchHistoryInteractionListener
import com.amsterdam.viewmodel.watchHistory.WatchHistoryUiState
import com.amsterdam.viewmodel.watchHistory.WatchHistoryViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WatchHistoryScreen(viewModel: WatchHistoryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigationManager = LocalNavManager.current
    WatchHistoryContent(state, viewModel, viewModel::onClickTabOption)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is WatchHistoryEffect.NavigateToMovieDetails -> {
                    navigationManager.toMovieDetails(effect.movieId)
                }

                is WatchHistoryEffect.NavigateToTvShowDetails -> {
                    navigationManager.toSeriesDetails(effect.tvShowId)
                }

                WatchHistoryEffect.NavigateBack -> {
                    navigationManager.navigateUp()
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
    val selectedMovies = state.movies.collectAsLazyPagingItems()
    val selectedTvShows = state.tvShows.collectAsLazyPagingItems()

    var headerHeight by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.onSizeChanged {
                headerHeight = it.height.dp
            }
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
        }

        AnimatedContent(state.selectedTabOption) { selectedTab ->
            if (selectedTab == TabOption.MOVIES && selectedMovies.itemCount != 0) {
                SuccessMovieMediaItemsSection(
                    onMovieClicked = interactionListener::onClickMovieCard,
                    selectedItems = selectedMovies
                )
            } else if (selectedTab == TabOption.TV_SHOWS && selectedTvShows.itemCount != 0) {
                SuccessTvShowMediaItemsSection(
                    onTvShowClicked = interactionListener::onClickTvShowCard,
                    selectedItems = selectedTvShows
                )
            } else {
                NoItemFoundContainer(
                    headerHeight = headerHeight,
                )
            }
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

@Composable
private fun NoItemFoundContainer(
    modifier: Modifier = Modifier,
    headerHeight: Dp = 0.dp,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        CenterOfScreenContainer(
            unneededSpace = headerHeight,
        ) {
            NoDataContainer(
                imageRes = painterResource(no_saved_items),
                title = stringResource(no_items_here),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}