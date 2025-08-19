package com.amsterdam.ui.screens.search.keywordSearch.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.LoadingIndicator
import com.amsterdam.designsystem.components.buttons.ButtonDefaults
import com.amsterdam.designsystem.components.buttons.PlainTextButton
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MediaType


@Composable
fun SuccessMediaItemsSection(
    onMovieClicked: (movieId: Long) -> Unit,
    onTvShowClicked: (tvShowId: Long) -> Unit,
    selectedItems: LazyPagingItems<SearchMediaItemUiState>
) {
    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
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
                                safetyLevel = safetyLevel,
                                contentScale = ContentScale.Crop,
                                onLoading = { ImageLoadingIndicator() },
                                onError = { ImageErrorIndicator() },
                                isAdult = mediaItem.isAdult
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
                                safetyLevel = safetyLevel,
                                model = mediaItem.posterImageUrl,
                                contentScale = ContentScale.Crop,
                                onLoading = { ImageLoadingIndicator() },
                                onError = { ImageErrorIndicator() },
                                isAdult = mediaItem.isAdult
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

        if (
            selectedItems.loadState.append is LoadState.Loading
            && selectedItems.itemCount > 1
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingIndicator(
                    Modifier
                        .size(48.dp)
                        .padding(top = 8.dp)
                )
            }
        }

        if (selectedItems.loadState.append is LoadState.Error) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                PlainTextButton(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 8.dp),
                    title = stringResource(com.amsterdam.ui.R.string.retry),
                    onClick = { selectedItems.retry() },
                    isEnabled = true,
                    isLoading = selectedItems.loadState.append is LoadState.Loading,
                    isNegative = false,
                    colors = ButtonDefaults.textButtonColors()
                )
            }
        }
    }
}
