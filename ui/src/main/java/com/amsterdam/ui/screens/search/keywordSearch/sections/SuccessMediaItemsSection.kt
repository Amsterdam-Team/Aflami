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
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MediaType


@Composable
fun SuccessMediaItemsSection(
    onMovieClicked: (movieId: Long) -> Unit,
    onTvShowClicked: (tvShowId: Long) -> Unit,
    selectedItems: LazyPagingItems<SearchMediaItemUiState>
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        items(
            count = selectedItems.itemCount,
        ) { index ->
            val mediaItem = selectedItems[index] ?: return@items
            when (mediaItem.mediaType) {
                MediaType.MOVIE -> {
                    MediaCard(
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

                MediaType.TV_SHOW -> {
                    MediaCard(
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
