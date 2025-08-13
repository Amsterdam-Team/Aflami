package com.amsterdam.ui.screens.categoriesDetails.movies

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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.LoadingIndicator
import com.amsterdam.designsystem.components.chip.Chip
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreIcon
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreLabel
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsInteractionListener
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsUiEffect
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsUiState
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsViewModel

@Composable
fun CategoriesMoviesDetailsScreen(
    viewModel: CategoriesMoviesDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current
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
                    navController.popBackStack()
                }

                is CategoriesMoviesDetailsUiEffect.NavigateToMovieDetails -> {
                    navController.navigate(
                        Route.MovieDetails(
                            movieId = effect.movieId
                        )
                    )
                }
            }
        }
    }
    CategoriesMoviesDetailsContent(
        state = state,
        interaction = viewModel,
        movies = movies

    )
}

@Composable
private fun CategoriesMoviesDetailsContent(
    state: CategoriesMoviesDetailsUiState,
    interaction: CategoriesMoviesDetailsInteractionListener,
    movies: LazyPagingItems<CategoriesMoviesDetailsUiState.MoviesUiState>
) {
    var appBarHeight by remember { mutableIntStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            DefaultAppBar(
                modifier = Modifier
                    .onSizeChanged { appBarHeight = it.height },
                title = stringResource(R.string.movies),
                onNavigateBackClicked = interaction::onClickBack
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
                    state.errorUiState != null && state.errorUiState is CategoriesMoviesDetailsUiState.CategoriesDetailsErrorState.NoNetworkConnection -> {
                        CenterOfScreenContainer(
                            unneededSpace = 0.dp
                        ) {
                            NoNetworkContainer(
                                onClickRetry = {
                                    interaction.onClickRetryRequest()
                                }
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
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
}

