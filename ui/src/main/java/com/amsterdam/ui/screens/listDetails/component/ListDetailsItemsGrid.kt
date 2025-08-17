package com.amsterdam.ui.screens.listDetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.LoadingIndicator
import com.amsterdam.designsystem.components.buttons.ButtonDefaults
import com.amsterdam.designsystem.components.buttons.PlainTextButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.listDetails.ListDetailsUiState.ListDetailsItemsUiState
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun ListDetailsItemsGrid(
    listMediaItems: LazyPagingItems<ListDetailsItemsUiState>,
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
    onClickMovie: (Long) -> Unit,
    onClickTvShow: (Long) -> Unit,
    onClickRemoveItem: (Long) -> Unit = {}
) {
    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = listMediaItems.itemCount,
            key = listMediaItems.itemKey { "${it.mediaType}${it.id}" }
        ) { index ->
            val listMediaItem = listMediaItems[index] ?: return@items

            val (topIcon, movieType, onCardClick) = when (listMediaItem.mediaType) {
                MediaType.MOVIE -> Triple(
                    painterResource(com.amsterdam.designsystem.R.drawable.ic_heart_remove),
                    stringResource(R.string.movies)
                ) { onClickMovie(listMediaItem.id) }

                else -> Triple(
                    null,
                    stringResource(R.string.tv_shows),
                ) { onClickTvShow(listMediaItem.id) }
            }

            MediaCard(
                modifier = Modifier.animateItem(),
                movieImage = {
                    SafeImageView(
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = listMediaItem.name,
                        model = listMediaItem.posterImageUrl,
                        safetyLevel = safetyLevel,
                        onLoading = { ImageLoadingIndicator() },
                        onError = { ImageErrorIndicator() },
                        isAdult = listMediaItem.isAdult
                    )
                },
                movieType = movieType,
                movieYear = listMediaItem.yearOfRelease,
                movieTitle = listMediaItem.name,
                movieRating = listMediaItem.rate,
                topIcon = topIcon,
                onTopIconClick = { onClickRemoveItem(listMediaItem.id) },
                onClick = onCardClick
            )
        }

        if (
            listMediaItems.loadState.append is LoadState.Loading
            && listMediaItems.itemCount > 1
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingIndicator(
                    Modifier
                        .size(48.dp)
                        .padding(top = 8.dp)
                )
            }
        }

        if (listMediaItems.loadState.append is LoadState.Error) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                PlainTextButton(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 8.dp),
                    title = stringResource(R.string.retry),
                    onClick = { listMediaItems.retry() },
                    isEnabled = true,
                    isLoading = listMediaItems.loadState.append is LoadState.Loading,
                    isNegative = false,
                    colors = ButtonDefaults.textButtonColors()
                )
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun MediaItemsGridPreview() {
    AflamiTheme {
        ListDetailsItemsGrid(
            listMediaItems = emptyFlow<PagingData<ListDetailsItemsUiState>>().collectAsLazyPagingItems(),
            gridState = rememberLazyGridState(),
            onClickMovie = {},
            onClickTvShow = {},
            onClickRemoveItem = {}
        )
    }
}