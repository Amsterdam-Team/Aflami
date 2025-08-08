package com.amsterdam.ui.screens.continueWatching.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.screens.search.actorSearch.MovieImage
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingUiState.ContinueWatchingItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import kotlinx.coroutines.flow.flowOf


@Composable
fun ContinueWatchingMediaItemsGrid(
    continueWatchingMediaItems:  LazyPagingItems<ContinueWatchingItemUiState>,
    onClickMediaItem: (Long, MediaType) -> Unit,
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
        items(continueWatchingMediaItems.itemCount, key = continueWatchingMediaItems.itemKey { it.id }){index->
            val item = continueWatchingMediaItems[index] !!
            val movieType =
                if (item.mediaType == MediaType.MOVIE) stringResource(com.amsterdam.ui.R.string.movie)
                else stringResource(com.amsterdam.ui.R.string.tv)

            MediaCard(
                movieImage = { MovieImage(item.posterImageUrl) },
                movieType = movieType,
                movieYear = item.yearOfRelease,
                movieTitle = item.name,
                movieRating = item.rate,
            ) {
                onClickMediaItem(item.id, item.mediaType)
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ContinueWatchingMoviesGridPreview() {
    AflamiTheme {
        val mockMovies = List(4) { index ->
            ContinueWatchingItemUiState(
                id = index.toLong(),
                name = "Movie $index",
                posterImageUrl = "",
                yearOfRelease = "202${index}",
                rate = (7 + index * 0.5).toString(),
                mediaType = MediaType.MOVIE
            )
        }

        val mockPagingFlow = remember { flowOf(PagingData.from(mockMovies)) }

        val lazyPagingItems = mockPagingFlow.collectAsLazyPagingItems()

        ContinueWatchingMediaItemsGrid(
            continueWatchingMediaItems = lazyPagingItems,
            onClickMediaItem = { _, _ -> }
        )
    }
}
