package com.example.ui.screens.home.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.designsystem.R
import com.example.designsystem.components.SectionTitle
import com.example.designsystem.theme.AppTheme
import com.example.ui.components.MovieCard
import com.example.ui.screens.search.actorSearch.MovieImage
import com.example.viewmodel.shared.uiStates.MovieItemUiState

fun LazyListScope.topRatingSection(
    topRatedMovies: List<MovieItemUiState>, onClickMovie: (Long) -> Unit,
    onClickShowAll: () -> Unit
) {
    item {
            SectionTitle(
                title = stringResource(R.string.top_rating),
                icon = painterResource(R.drawable.ic_fire),
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
            items(topRatedMovies) { movie ->
                MovieCard(
                    movieImage = { MovieImage(movie.posterImageUrl) },
                    movieType = stringResource(com.example.ui.R.string.movie),
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