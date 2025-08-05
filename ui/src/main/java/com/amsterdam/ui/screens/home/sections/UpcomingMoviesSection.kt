package com.amsterdam.ui.screens.home.sections

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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.chip.Chip
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.components.adaptiveGrid
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.screens.home.sections.placeholder.upcomingMoviesSectionPlaceholder
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreIcon
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreLabel
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.home.HomeUiState.UpcomingMoviesSectionUiState

fun LazyListScope.upcomingMoviesSection(
    state: UpcomingMoviesSectionUiState,
    deviceWidth: Int,
    onMovieClicked: (movieId: Long) -> Unit,
    onChangeMovieGenre: (genreType: MovieGenre) -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onVerticalOffsetChange: (Dp) -> Unit
) {
    if (isVisible) {
        if (state.isLoading) {
            upcomingMoviesSectionPlaceholder()
        } else {
            stickyHeader {
                Column(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .background(AppTheme.color.surface)
                        .padding(top = 24.dp, bottom = 16.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        text = stringResource(R.string.upcoming),
                        style = AppTheme.textStyle.title.medium,
                        color = AppTheme.color.title,
                        textAlign = TextAlign.Start,
                    )
                }
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .background(AppTheme.color.surface)
                        .onGloballyPositioned { onVerticalOffsetChange(it.positionOnScreen().y.dp) }
                    ,
                    contentPadding = PaddingValues(horizontal = 16.dp),
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

            if (state.movies.isNotEmpty()) {
                adaptiveGrid(
                    items = state.movies,
                    itemMinWidth = 320,
                    modifier = Modifier.padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    ),
                    itemsHorizontalPadding = 8.dp,
                    itemsVerticalPadding = 8.dp,
                    deviceWidth = deviceWidth,
                ) { movie ->
                    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
                    MediaCard(
                        modifier = modifier
                            .weight(1f)
                            .height(196.dp),
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = movie.name,
                                model = movie.posterImageUrl,
                                safetyLevel = safetyLevel,
                                onLoading = { ImageLoadingIndicator() },
                                onError = { ImageErrorIndicator() },
                            )
                        },
                        movieType = stringResource(R.string.movies),
                        movieYear = movie.yearOfRelease,
                        movieTitle = movie.name,
                        movieRating = movie.rate,
                        onClick = { onMovieClicked(movie.id) }
                    )
                }
            } else {
                item {
                    CenterOfScreenContainer(
                        unneededSpace = 230.dp
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(top = 130.dp)
                                .padding(16.dp),
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
}