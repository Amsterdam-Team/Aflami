package com.example.ui.screens.search.searchByCountry.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.designsystem.R
import com.example.designsystem.components.MovieCard
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.viewmodel.search.countrySearch.MovieUiState

@Composable
internal fun MoviesVerticalGrid(
    movies: LazyPagingItems<MovieUiState>,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        movies.itemSnapshotList.isNotEmpty() && isVisible,
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(160.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 4.dp),
        ) {
            items(movies.itemCount) { index ->
                val movie = movies[index] ?: return@items
                MovieCard(
                    movieImage = movie.poster,
                    movieType = stringResource(R.string.movie),
                    movieYear = movie.productionYear,
                    movieTitle = movie.name,
                    movieRating = movie.rating,
                )
            }
            item {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun MoviesVerticalGridPreview() {
    AflamiTheme {
//        MoviesVerticalGrid(
//            movies = buildList(4) { MovieUiState(
//                id = 1,
//                name = stringResource(R.string.movie),
//                poster = "https://unsplash.com/s/photos/free-images",
//                productionYear = "2025",
//                rating = "9.9"
//            ) },
//            isVisible = true,
//        )
    }
}