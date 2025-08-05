package com.amsterdam.ui.screens.search.countrySearch.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun MoviesVerticalGrid(
    movies: LazyPagingItems<MovieItemUiState>,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onMovieClicked: (movieId: Long) -> Unit,
) {
    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
    AnimatedVisibility(isVisible) {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Adaptive(160.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            items(
                count = movies.itemCount,
            ) { index ->
                val movie = movies[index] ?: return@items
                MediaCard(
                    movieImage = {
                        SafeImageView(
                            modifier =
                                Modifier
                                    .fillMaxSize(),
                            contentDescription = movie.name,
                            model = movie.posterImageUrl,
                            safetyLevel = safetyLevel,
                            contentScale = ContentScale.Crop,
                            onLoading = { ImageLoadingIndicator() },
                            onError = { ImageErrorIndicator() },
                        )
                    },
                    movieType = stringResource(R.string.movie),
                    movieYear = movie.yearOfRelease,
                    movieTitle = movie.name,
                    movieRating = movie.rate,
                    onClick = { onMovieClicked(movie.id) }
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
            movies = emptyFlow<PagingData<MovieItemUiState>>().collectAsLazyPagingItems(),
            isVisible = true,
            onMovieClicked = {}
        )
    }
}