package com.amsterdam.ui.screens.categoriesDetails.tvShow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.LoadingIndicator
import com.amsterdam.designsystem.components.chip.Chip
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getTvShowGenreIcon
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getTvShowGenreLabel
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsInteractionListener
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsUiEffect
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsUiState
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsViewModel
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState

@Composable
fun CategoriesTvShowsDetailsScreen(
    viewModel: CategoriesTvShowsDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val navigationManager = LocalNavManager.current
    val tvShows = state.tvShows.collectAsLazyPagingItems()

    LaunchedEffect(tvShows.loadState) {
        viewModel.onPagingLoadStateChanged(tvShows.loadState)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CategoriesTvShowsDetailsUiEffect.NavigateBack -> {
                    navigationManager.navigateUp()
                }

                is CategoriesTvShowsDetailsUiEffect.NavigateToTvShowDetails -> {
                    navigationManager.toSeriesDetails(
                        tvShowId = effect.tvShowId
                    )
                }
            }
        }
    }
    CategoriesTvShowsDetailsContent(
        state = state,
        errorState = errorState,
        interactionListener = viewModel,
        tvShows = tvShows
    )
}

@Composable
private fun CategoriesTvShowsDetailsContent(
    state: CategoriesTvShowsDetailsUiState,
    errorState: ErrorUiState?,
    interactionListener: CategoriesTvShowsDetailsInteractionListener,
    tvShows: LazyPagingItems<CategoriesTvShowsDetailsUiState.TvShowsUiState>
) {
    Box(
        modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            DefaultAppBar(
                title = stringResource(R.string.tv_shows),
                onNavigateBackClicked = interactionListener::onClickBack
            )
            Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.tvShowGenres) { genreItem ->
                        val genre = genreItem.selectableTvShowGenre.item
                        if (genre == TvShowGenre.ALL) return@items

                        Chip(
                            icon = getTvShowGenreIcon(genre),
                            label = getTvShowGenreLabel(genre),
                            isSelected = genreItem.selectableTvShowGenre.isSelected,
                            onClick = { interactionListener.onClickGenre(genre) },
                        )
                    }

                }
                when {
                    state.isLoading -> {
                        CenterOfScreenContainer(unneededSpace = 0.dp) {
                            AnimatedVisibility(visible = state.isLoading) {
                                LoadingIndicator()
                            }
                        }
                    }

                    errorState is ErrorUiState.NoInternetError -> {
                        CenterOfScreenContainer(unneededSpace = 0.dp) {
                            NoNetworkContainer(onClickRetry = interactionListener::onClickRetryRequest)
                        }
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(242.dp),
                            modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(tvShows.itemCount) { index ->
                                val tvShow = tvShows[index] ?: return@items
                                MediaCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    movieImage = {
                                        SafeImageView(
                                            model = tvShow.posterImageUrl,
                                            contentDescription = tvShow.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxWidth(),
                                            onLoading = {
                                                ImageLoadingIndicator()
                                            },
                                            onError = {
                                                ImageErrorIndicator()
                                            },
                                            isAdult = tvShow.isAdult
                                        )
                                    },
                                    movieTitle = tvShow.name,
                                    movieType = stringResource(R.string.tv),
                                    movieYear = tvShow.yearOfRelease,
                                    movieRating = tvShow.rate,
                                    onClick = { interactionListener.onClickTvShowCard(tvShow.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}