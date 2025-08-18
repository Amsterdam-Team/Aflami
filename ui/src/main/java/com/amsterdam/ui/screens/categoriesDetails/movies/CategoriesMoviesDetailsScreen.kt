package com.amsterdam.ui.screens.categoriesDetails.movies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
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
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreIcon
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreLabel
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsInteractionListener
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsUiEffect
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsUiState
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsViewModel
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState

@Composable
fun CategoriesMoviesDetailsScreen(
    viewModel: CategoriesMoviesDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val navigationManager = LocalNavManager.current
    val movies = state.movies.collectAsLazyPagingItems()
    LaunchedEffect(
        key1 = movies.loadState,
    ) {
        viewModel.onPagingLoadStateChanged(movies.loadState)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CategoriesMoviesDetailsUiEffect.NavigateBack -> {
                    navigationManager.navigateUp()
                }

                is CategoriesMoviesDetailsUiEffect.NavigateToMovieDetails -> {
                    navigationManager.toMovieDetails(
                        movieId = effect.movieId
                    )
                }
            }
        }
    }
    CategoriesMoviesDetailsContent(
        state = state,
        errorState = errorState,
        interaction = viewModel,
        movies = movies

    )
}

@Composable
private fun CategoriesMoviesDetailsContent(
    state: CategoriesMoviesDetailsUiState,
    errorState: ErrorUiState?,
    interaction: CategoriesMoviesDetailsInteractionListener,
    movies: LazyPagingItems<CategoriesMoviesDetailsUiState.MoviesUiState>
) {
    Column(
        Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
    ) {
        DefaultAppBar(
            title = stringResource(R.string.movies),
            onNavigateBackClicked = interaction::onClickBack
        )
        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.movieGenres) { genreItem ->
                    val genre = genreItem.selectableMovieGenre.item
                    if (genre == MovieGenre.ALL) return@items

                    Chip(
                        icon = getMovieGenreIcon(genre),
                        label = getMovieGenreLabel(genre),
                        isSelected = genreItem.selectableMovieGenre.isSelected,
                        onClick = { interaction.onClickGenre(genre) },
                    )
                }

            }
            when {
                state.isLoading -> {
                    CenterOfScreenContainer(
                        unneededSpace = 0.dp
                    ) {
                        AnimatedVisibility(visible = state.isLoading) {
                            LoadingIndicator()
                        }
                    }
                }

                errorState is ErrorUiState.NoInternetError -> {
                    CenterOfScreenContainer(unneededSpace = 0.dp) {
                        NoNetworkContainer(onClickRetry = interaction::onClickRetryRequest)
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(242.dp),
                        modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(movies.itemCount) { index ->
                            val movie = movies[index] ?: return@items
                            MediaCard(
                                modifier = Modifier.fillMaxWidth(),
                                movieImage = {
                                    SafeImageView(
                                        model = movie.posterImageUrl,
                                        contentDescription = movie.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxWidth(),
                                        onLoading = {
                                            ImageLoadingIndicator()
                                        },
                                        onError = {
                                            ImageErrorIndicator()
                                        },
                                    )
                                },
                                movieTitle = movie.name,
                                movieType = stringResource(R.string.movie),
                                movieYear = movie.yearOfRelease,
                                movieRating = movie.rate,
                                onClick = { interaction.onClickMovieCard(movie.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

