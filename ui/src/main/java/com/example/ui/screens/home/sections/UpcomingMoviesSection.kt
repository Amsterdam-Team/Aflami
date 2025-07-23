package com.example.ui.screens.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.designsystem.components.ImageErrorIndicator
import com.example.designsystem.components.ImageLoadingIndicator
import com.example.designsystem.components.Text
import com.example.designsystem.components.chip.Chip
import com.example.designsystem.theme.AppTheme
import com.example.entity.category.MovieGenre
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.components.MovieCard
import com.example.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreIcon
import com.example.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreLabel
import com.example.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.example.viewmodel.shared.uiStates.MovieItemUiState

fun LazyListScope.upcomingMoviesSection(
    movies: List<MovieItemUiState>,
    onMovieClicked: (movieId: Long) -> Unit,
    moviesGenres: List<MovieGenreItemUiState>,
    onChangeMovieGenre: (genreType: MovieGenre) -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    stickyHeader {
        AnimatedSectionVisibility(isVisible) {
            Column {
                Text(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .background(AppTheme.color.surface)
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp),
                    text = stringResource(R.string.upcoming),
                    style = AppTheme.textStyle.title.medium,
                    color = AppTheme.color.title,
                    textAlign = TextAlign.Start,
                )

                LazyRow(
                    modifier = Modifier.background(AppTheme.color.surface),
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(moviesGenres) { genreItem ->
                        val genre = genreItem.selectableMovieGenre.item
                        Chip(
                            icon = getMovieGenreIcon(genre),
                            label = getMovieGenreLabel(genre),
                            isSelected = genreItem.selectableMovieGenre.isSelected,
                            onClick = { onChangeMovieGenre(genre) },
                        )
                    }
                }
            }
        }
    }

    items(items = movies, key = { it.id }) { movie ->
        AnimatedSectionVisibility(isVisible) {
            with(movie) {
                MovieCard(
                    modifier = modifier
                        .fillParentMaxWidth()
                        .animateItem()
                        .height(196.dp)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    movieImage = {
                        SafeImageView(
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = name,
                            model = posterImageUrl,
                            onLoading = { ImageLoadingIndicator() },
                            onError = { ImageErrorIndicator() },
                        )
                    },
                    movieType = stringResource(R.string.movies),
                    movieYear = yearOfRelease,
                    movieTitle = name,
                    movieRating = rate,
                    onClick = { onMovieClicked(id) }
                )
            }
        }
    }
}