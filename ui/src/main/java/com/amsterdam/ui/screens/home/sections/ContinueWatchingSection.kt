package com.amsterdam.ui.screens.home.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.ui.R
import androidx.compose.ui.zIndex
import com.amsterdam.designsystem.components.SectionTitle
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.MovieCard
import com.amsterdam.ui.screens.home.sections.placeholder.movieSectionPlaceholder
import com.amsterdam.ui.screens.search.actorSearch.MovieImage
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingUiState
import com.amsterdam.viewmodel.home.HomeUiState


fun LazyListScope.continueWatchingSection(
    state: HomeUiState.ContinueWatchingMoviesSectionUiState,
    onClickMovie: (Long) -> Unit,
    onClickShowAll: () -> Unit,
    isVisible: Boolean
) {
    if (isVisible){
        if (state.isLoading){
            movieSectionPlaceholder()
        } else {
            item {
                SectionTitle(
                    title = stringResource(R.string.continue_watching),
                    tintColor = AppTheme.color.secondary,
                    modifier = Modifier
                        .zIndex(1f)
                        .padding(top = 24.dp, bottom = 12.dp),
                    showAllLabel = true,
                    onAllLabelClicked = onClickShowAll
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(state.movies) { movie ->
                        MovieCard(
                            movieImage = { MovieImage(movie.posterImageUrl) },
                            movieType = stringResource(com.amsterdam.ui.R.string.movie),
                            movieYear = movie.yearOfRelease,
                            movieTitle = movie.name,
                            movieRating = movie.rate
                        ) {
                            onClickMovie(movie.id)
                        }
                    }
                }
            }
        }
    }
}