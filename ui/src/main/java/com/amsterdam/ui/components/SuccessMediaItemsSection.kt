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
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.watchHistory.WatchHistoryUiState

@Composable
fun SuccessMediaItemsSection(
    onMovieClicked: (movieId: Long) -> Unit,
    onTvShowClicked: (tvShowId: Long) -> Unit,
    selectedItems: LazyPagingItems<WatchHistoryUiState.WatchHistoryItemUiState>
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
            val selectedItem = selectedItems[index] ?: return@items
            val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()

            when (selectedItem.mediaType) {
                 MediaType.MOVIE -> {
                    MediaCard(
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = selectedItem.name,
                                model = selectedItem.posterImageUrl,
                                contentScale = ContentScale.Crop, safetyLevel = safetyLevel,
                                onLoading = { ImageLoadingIndicator() },
                                onError = { ImageErrorIndicator() },
                            )
                        },
                        movieType = stringResource(R.string.movies),
                        movieYear = selectedItem.yearOfRelease,
                        movieTitle = selectedItem.name,
                        movieRating = selectedItem.rate,
                        onClick = { onMovieClicked(selectedItem.id) }
                    )
                }

                MediaType.TV_SHOW  -> {
                    MediaCard(
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = selectedItem.name,
                                model = selectedItem.posterImageUrl,
                                contentScale = ContentScale.Crop,
                                safetyLevel = safetyLevel, onLoading = { ImageLoadingIndicator() },
                                onError = { ImageErrorIndicator() },
                            )
                        },
                        movieType = stringResource(R.string.tv_shows),
                        movieYear = selectedItem.yearOfRelease,
                        movieTitle = selectedItem.name,
                        movieRating = selectedItem.rate,
                        onClick = { onTvShowClicked(selectedItem.id) }
                    )
                }
            }
        }
    }
}

