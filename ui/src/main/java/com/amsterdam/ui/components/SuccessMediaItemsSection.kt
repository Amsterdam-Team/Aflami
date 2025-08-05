package com.amsterdam.ui.components

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
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState

@Composable
fun SuccessMediaItemsSection(
    onMovieClicked: (movieId: Long) -> Unit,
    onTvShowClicked: (tvShowId: Long) -> Unit,
    selectedItems: LazyPagingItems<out Any>
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
            val mediaItem = selectedItems[index]
            val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
            when (mediaItem) {
                is MovieItemUiState -> {
                    MediaCard(
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = mediaItem.name,
                                model = mediaItem.posterImageUrl,
                                contentScale = ContentScale.Crop, safetyLevel = safetyLevel,
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
                    MediaCard(
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = mediaItem.name,
                                model = mediaItem.posterImageUrl,
                                contentScale = ContentScale.Crop,
                                safetyLevel = safetyLevel, onLoading = { ImageLoadingIndicator() },
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

