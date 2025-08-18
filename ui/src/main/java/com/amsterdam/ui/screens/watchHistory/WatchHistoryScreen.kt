package com.amsterdam.ui.screens.watchHistory

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.TabsLayout
import com.amsterdam.ui.R
import com.amsterdam.ui.R.drawable.no_saved_items
import com.amsterdam.ui.R.string.no_items_here
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.ui.screens.watchHistory.sections.SuccessMovieMediaItemsSection
import com.amsterdam.ui.screens.watchHistory.sections.SuccessTvShowMediaItemsSection
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.NoInternetError
import com.amsterdam.viewmodel.watchHistory.WatchHistoryEffect
import com.amsterdam.viewmodel.watchHistory.WatchHistoryInteractionListener
import com.amsterdam.viewmodel.watchHistory.WatchHistoryUiState
import com.amsterdam.viewmodel.watchHistory.WatchHistoryViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WatchHistoryScreen(viewModel: WatchHistoryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val navigationManager = LocalNavManager.current

    WatchHistoryContent(state, errorState, viewModel, viewModel::onClickTabOption)
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
    errorState: ErrorUiState?,
    interactionListener: WatchHistoryInteractionListener,
    onTabOptionClicked: (TabOption) -> Unit,
) {
    val density = LocalDensity.current
    val moviesPaging =
        state.movies.collectAsLazyPagingItems<WatchHistoryUiState.WatchHistoryMovieUiState>()
    val tvShowsPaging =
        state.tvShows.collectAsLazyPagingItems<WatchHistoryUiState.WatchHistoryTvShowUiState>()

    var headerHeight by remember { mutableStateOf(0.dp) }
    var lastSelectedTab by remember { mutableStateOf(state.selectedTabOption) }

    var moviesEmpty by remember { mutableStateOf<Boolean?>(null) }
    var tvShowsEmpty by remember { mutableStateOf<Boolean?>(null) }
    var moviesFirstLoadDone by remember { mutableStateOf(false) }
    var tvShowsFirstLoadDone by remember { mutableStateOf(false) }

    LaunchedEffect(moviesPaging.itemCount, moviesPaging.loadState.refresh) {
        if (moviesPaging.loadState.refresh !is LoadState.Loading) {
            moviesFirstLoadDone = true
            if (moviesEmpty == null) {
                moviesEmpty = moviesPaging.itemCount == 0
            }
        }
    }

    LaunchedEffect(tvShowsPaging.itemCount, tvShowsPaging.loadState.refresh) {
        if (tvShowsPaging.loadState.refresh !is LoadState.Loading) {
            tvShowsFirstLoadDone = true
            if (tvShowsEmpty == null) {
                tvShowsEmpty = tvShowsPaging.itemCount == 0
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.onSizeChanged {
                headerHeight = with(density) { it.height.dp }
            }
        ) {
            DefaultAppBar(
                title = stringResource(R.string.watch_history),
                modifier = Modifier
                    .fillMaxWidth(),
                onNavigateBackClicked = interactionListener::onClickBack
            )

            TabsLayout(
                modifier = Modifier.fillMaxWidth(),
                tabs = listOf(
                    stringResource(R.string.movies),
                    stringResource(R.string.tv_shows),
                ),
                selectedIndex = state.selectedTabOption.index,
                onSelectTab = { index ->
                    val newTab = TabOption.entries[index]
                    if (newTab != lastSelectedTab) {
                        lastSelectedTab = newTab
                        onTabOptionClicked(newTab)
                    }
                },
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedSectionVisibility(visible = state.selectedTabOption == TabOption.MOVIES) {
                when {
                    !moviesFirstLoadDone &&
                            moviesPaging.loadState.refresh is LoadState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { LoadingContainer() }
                    }

                    moviesPaging.itemCount > 0 -> {
                        SuccessMovieMediaItemsSection(
                            onMovieClicked = interactionListener::onClickMovieCard,
                            selectedItems = moviesPaging
                        )
                    }

                    moviesEmpty == true -> {
                        NoItemFoundContainer(headerHeight = headerHeight)
                    }
                }
            }

            AnimatedSectionVisibility(visible = state.selectedTabOption == TabOption.TV_SHOWS) {
                when {
                    !tvShowsFirstLoadDone &&
                            tvShowsPaging.loadState.refresh is LoadState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { LoadingContainer() }
                    }

                    tvShowsPaging.itemCount > 0 -> {
                        SuccessTvShowMediaItemsSection(
                            onTvShowClicked = interactionListener::onClickTvShowCard,
                            selectedItems = tvShowsPaging
                        )
                    }

                    tvShowsEmpty == true -> {
                        NoItemFoundContainer(headerHeight = headerHeight)
                    }
                }
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

        AnimatedSectionVisibility(visible = errorState is NoInternetError) {
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
                modifier = Modifier.padding(top = 12.dp),
                imageAlpha = 0.68f,
                textAlpha = 0.68f
            )
        }
    }
}