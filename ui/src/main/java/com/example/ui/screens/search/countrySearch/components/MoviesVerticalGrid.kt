package com.example.ui.screens.search.countrySearch.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.components.MovieCard
import com.example.viewmodel.search.countrySearch.MovieUiState

@Composable
internal fun MoviesVerticalGrid(
    movies: List<MovieItemUiState>,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onMovieClicked: (movieId: Long) -> Unit,
) {
    AnimatedVisibility(isVisible) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(160.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            items(
                items = movies,
                key = { movie -> movie.id },
            ) { movie ->
                MovieCard(
                    movieImage = movie.posterImageUrl,
                    movieType = stringResource(R.string.movie),
                    movieYear = movie.yearOfRelease,
                    movieTitle = movie.name,
                    movieRating = movie.rate,
                    onClick = {onMovieClicked(movie.id) }
                )
            }
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun MoviesVerticalGridPreview() {
    AflamiTheme {
        MoviesVerticalGrid(
            movies =
                buildList(4) {
                    MovieUiState(
                        id = 1,
                        name = stringResource(R.string.movie),
                        poster = "https://unsplash.com/s/photos/free-images",
                        productionYear = "2025",
                        rating = "9.9",
                    )
                },
            isVisible = true,
            onMovieClicked = {}
        )
    }
}