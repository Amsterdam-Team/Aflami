package com.amsterdam.ui.screens.topRated.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.components.MovieCard
import com.amsterdam.ui.screens.search.actorSearch.MovieImage
import com.amsterdam.ui.utils.formateAsRate
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType

@Composable
fun TopRatedMediaItemsGrid(
    topRatedMediaItems: List<MediaItemUiState>,
    onClickMediaItem: (Long, MediaType) -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(topRatedMediaItems) { item ->
            val movieType =
                if (item.mediaType == MediaType.MOVIE) stringResource(com.amsterdam.ui.R.string.movie)
                else stringResource(com.amsterdam.ui.R.string.tv)

            MovieCard(
                movieImage = { MovieImage(item.posterImageUrl) },
                movieType = movieType,
                movieYear = item.yearOfRelease,
                movieTitle = item.name,
                movieRating = item.rate.formateAsRate()
            ) {
                onClickMediaItem(item.id, item.mediaType)
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun TopRatedMoviesGridPreview() {
    AflamiTheme {
        val mockMovies = List(4) { index ->
            MediaItemUiState(
                id = index.toLong(),
                name = "Movie $index",
                posterImageUrl = "",
                yearOfRelease = "202${index}",
                rate = (7 + index * 0.5).toString()
            )
        }
        TopRatedMediaItemsGrid(
            topRatedMediaItems = mockMovies,
            onClickMediaItem = { _, _ -> }
        )
    }
}