package com.amsterdam.ui.screens.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.ui.screens.home.sections.placeholder.upcomingMoviesSectionPlaceholder
import com.amsterdam.viewmodel.home.HomeUiState.UpcomingMoviesSectionUiState
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.chip.Chip
import com.amsterdam.ui.R
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.components.MovieCard
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreIcon
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreLabel

fun LazyListScope.upcomingMoviesSection(
    state: UpcomingMoviesSectionUiState,
    onMovieClicked: (movieId: Long) -> Unit,
    onChangeMovieGenre: (genreType: MovieGenre) -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    if (isVisible){
        if (state.isLoading) {
            upcomingMoviesSectionPlaceholder()
        } else {
            stickyHeader {
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
                        items(state.movieGenres) { genreItem ->
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

            if (state.movies.isNotEmpty()) {
                items(items = state.movies, key = { it.id }) { movie ->
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
            } else {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        text = stringResource(R.string.no_upcoming_movies_found_for_your_selection),
                        style = AppTheme.textStyle.label.medium,
                        color = AppTheme.color.body,
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }

}