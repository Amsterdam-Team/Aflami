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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.components.MovieCard
import com.amsterdam.ui.screens.search.actorSearch.MovieImage
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun TopRatedMediaItemsGrid(
    onClickMediaItem: (Long, MediaType) -> Unit,
    movies: LazyPagingItems<MovieItemUiState>,
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
        items(
            count = movies.itemCount,
            key = { index -> "${movies[index]?.id}-$index" },
        ) { index ->
            val movie = movies[index] ?: return@items
            MovieCard(
                movieImage = { MovieImage(movie.posterImageUrl) },
                movieType = stringResource(R.string.movie),
                movieYear = movie.yearOfRelease,
                movieTitle = movie.name,
                movieRating = movie.rate,
            ) {
               onClickMediaItem(movie.id, MediaType.MOVIE)
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
            movies = emptyFlow<PagingData<MovieItemUiState>>().collectAsLazyPagingItems(),
            )
    }
}