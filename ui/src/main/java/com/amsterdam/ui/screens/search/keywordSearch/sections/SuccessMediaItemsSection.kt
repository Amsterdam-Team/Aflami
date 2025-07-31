package com.amsterdam.ui.screens.search.keywordSearch.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.components.MovieCard
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiState
import com.amsterdam.viewmodel.search.keywordSearch.TabOption
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState

@Composable
fun SuccessMediaItemsSection(
    selectedTabOption: TabOption,
    moviesFlow: LazyPagingItems<MovieItemUiState>,
    tvShowsFlow: LazyPagingItems<TvShowItemUiState>,
    onMovieClicked: (movieId: Long) -> Unit,
    onTvShowClicked: (tvShowId: Long) -> Unit,
    state: SearchUiState,
) {
    val selectedItems = if (selectedTabOption == TabOption.MOVIES) {
        moviesFlow
    } else {
        tvShowsFlow
    }

    if (!state.isLoading && state.errorUiState == null && selectedItems.itemCount > 0) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            items(
                count = selectedItems.itemCount,
                key = { index -> getItemKey(selectedTabOption, index, selectedItems) },
            ) { index ->
                val mediaItem = selectedItems[index]
                when (mediaItem) {
                    is MovieItemUiState -> {
                        MovieCard(
                            movieImage = {
                                SafeImageView(
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = mediaItem.name,
                                    model = mediaItem.posterImageUrl,
                                    contentScale = ContentScale.Crop,
                                    onLoading = { ImageLoadingIndicator() },
                                    onError = { ImageErrorIndicator() },
                                )
                            },
                            movieType = stringResource(R.string.movies),
                            movieYear = mediaItem.yearOfRelease,
                            movieTitle = mediaItem.name,
                            movieRating = mediaItem.rate,
                            onClick = { onMovieClicked(mediaItem.id) }
                        )
                    }

                    is TvShowItemUiState -> {
                        MovieCard(
                            movieImage = {
                                SafeImageView(
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = mediaItem.name,
                                    model = mediaItem.posterImageUrl,
                                    contentScale = ContentScale.Crop,
                                    onLoading = { ImageLoadingIndicator() },
                                    onError = { ImageErrorIndicator() },
                                )
                            },
                            movieType = stringResource(R.string.tv_shows),
                            movieYear = mediaItem.yearOfRelease,
                            movieTitle = mediaItem.name,
                            movieRating = mediaItem.rate,
                            onClick = { onTvShowClicked(mediaItem.id) }
                        )
                    }
                }
            }
        }
    }
}

private fun getItemKey(
    selectedTabOption: TabOption,
    index: Int,
    selectedItems: LazyPagingItems<out Any>
): String {
    val item = selectedItems[index]
    val id = if (selectedTabOption == TabOption.MOVIES) (item as MovieItemUiState).id
    else (item as TvShowItemUiState).id
    return "${id}-${index}"
}