package com.example.ui.screens.movieDetails.components

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
import com.example.designsystem.R
import com.example.designsystem.components.ImageErrorIndicator
import com.example.designsystem.components.ImageLoadingIndicator
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.components.UpcomingCard
import com.example.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState

fun LazyListScope.MoreLikeSection(similarMovies: List<SimilarMovieUiState>) {
    if (similarMovies.isEmpty())
        item {
            EmptyStateText(stringResource(com.example.ui.R.string.there_is_no_production_company))
        }
    else
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
            )
        }
}
