package com.amsterdam.ui.screens.search.countrySearch.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.LoadingIndicator
import com.amsterdam.designsystem.components.buttons.ButtonDefaults
import com.amsterdam.designsystem.components.buttons.PlainTextButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun MoviesVerticalGrid(
    movies: LazyPagingItems<SearchMediaItemUiState>,
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
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
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
                            isAdult = movie.isAdult
                        )
                    },
                    movieType = stringResource(R.string.movie),
                    movieYear = movie.yearOfRelease,
                    movieTitle = movie.name,
                    movieRating = movie.rate,
                    onClick = { onMovieClicked(movie.id) }
                )
            }

            if (
                movies.loadState.append is LoadState.Loading
                && movies.itemCount > 1
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LoadingIndicator(
                        Modifier
                            .size(48.dp)
                            .padding(top = 8.dp)
                    )
                }
            }

            if (movies.loadState.append is LoadState.Error) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    PlainTextButton(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(top = 8.dp),
                        title = stringResource(com.amsterdam.ui.R.string.retry),
                        onClick = { movies.retry() },
                        isEnabled = true,
                        isLoading = movies.loadState.append is LoadState.Loading,
                        isNegative = false,
                        colors = ButtonDefaults.textButtonColors()
                    )
                }
            }
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun MoviesVerticalGridPreview() {
    AflamiTheme {
        MoviesVerticalGrid(
            movies = emptyFlow<PagingData<SearchMediaItemUiState>>().collectAsLazyPagingItems(),
            isVisible = true,
            onMovieClicked = {}
        )
    }
}