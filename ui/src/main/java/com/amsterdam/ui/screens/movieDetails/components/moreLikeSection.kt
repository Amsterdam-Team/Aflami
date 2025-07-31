package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.components.UpcomingCard
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState

fun LazyListScope.moreLikeSection(
    similarMovies: List<SimilarMovieUiState>,
    onClick: (movieId: Long) -> Unit
) {
    if (similarMovies.isEmpty()) {
        item { EmptyStateText(stringResource(com.amsterdam.ui.R.string.there_is_no_similar_content)) }
    } else {
        itemsIndexed(similarMovies, key = { index, _ -> index }) { index, similarMovie ->
            val yOffset = if (index == 0) -16 else 0
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
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    .offset(y = yOffset.dp),
                movieRating = similarMovie.rate,
                onClick = { onClick(similarMovie.movieId) }
            )
        }
    }
}
