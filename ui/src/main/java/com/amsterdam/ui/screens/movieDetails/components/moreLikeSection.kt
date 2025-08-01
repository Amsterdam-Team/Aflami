package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.components.UpcomingCard
import com.amsterdam.ui.components.adaptiveGrid
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState

fun LazyListScope.moreLikeSection(
    similarMovies: List<SimilarMovieUiState>,
    deviceWidth: Int,
    onClick: (movieId: Long) -> Unit,
) {
    if (similarMovies.isEmpty()) {
        item { EmptyStateText(stringResource(com.amsterdam.ui.R.string.there_is_no_similar_content)) }
    } else {
        adaptiveGrid(
            deviceWidth = deviceWidth,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            items = similarMovies, itemMinWidth = 328,
            itemsHorizontalPadding = 8.dp, itemsVerticalPadding = 8.dp
        ) { similarMovie ->
            UpcomingCard(
                movieImage = {
                    SafeImageView(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                        contentDescription = similarMovie.name,
                        model = similarMovie.posterUrl,
                        contentScale = ContentScale.Crop,
                        onLoading = { ImageLoadingIndicator() },
                        onError = { ImageErrorIndicator() },
                    )
                },
                movieTitle = similarMovie.name,
                movieType = stringResource(R.string.movie),
                movieYear = similarMovie.productionYear,
                modifier = Modifier
                    .weight(1f),
                movieRating = similarMovie.rate,
                onClick = { onClick(similarMovie.movieId) }
            )
        }
    }
}
