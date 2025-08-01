package com.amsterdam.ui.screens.topRated.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.screens.search.actorSearch.MovieImage
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun TopRatedMediaItemsGrid(
    onClickMediaItem: (Long, MediaType) -> Unit,
    mediaItems: LazyPagingItems<MediaItemUiState>,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState()
) {
    
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        state = gridState,
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = mediaItems.itemCount,
        ) { index ->
            val media = mediaItems[index] ?: return@items
            val mediaType = when (media.mediaType) {
                MediaType.MOVIE -> stringResource(R.string.movie)
                MediaType.TV_SHOW -> stringResource(R.string.tv_shows)
            }
            MediaCard(
                movieImage = { MovieImage(media.posterImageUrl) },
                movieType = mediaType,
                movieYear = media.yearOfRelease,
                movieTitle = media.name,
                movieRating = media.rate,
            ) {
                onClickMediaItem(media.id, media.mediaType)
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun TopRatedMoviesGridPreview() {
    AflamiTheme {
        TopRatedMediaItemsGrid(
            onClickMediaItem = { _, _ -> },
            mediaItems = emptyFlow<PagingData<MediaItemUiState>>().collectAsLazyPagingItems(),
        )
    }
}