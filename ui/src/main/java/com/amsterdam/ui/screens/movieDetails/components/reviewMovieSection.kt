package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.R
import com.amsterdam.ui.components.EmptyStateText
import com.amsterdam.ui.components.movieAndTvShowDetails.ReviewCard
import com.amsterdam.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.ReviewMovieUiState

fun LazyListScope.reviewMovieSection(
    reviews: List<ReviewMovieUiState>,
    interactionListener: MovieDetailsInteractionListener
) {
    if (reviews.isEmpty()) {
        item { EmptyStateText(stringResource(R.string.there_is_no_reviews)) }
    } else {
        items(reviews) { item ->
            ReviewCard(
                author = item.author,
                username = item.username,
                rating = item.rating,
                content = item.content,
                date = item.date,
                imageUrl = item.author,
                isExpanded = item.isExpanded,
                onToggleExpansion = { interactionListener.onReviewExpansionToggled(item.username) }
            )
        }
    }
}