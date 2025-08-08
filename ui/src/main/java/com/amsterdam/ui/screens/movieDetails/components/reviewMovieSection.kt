package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource
import com.amsterdam.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.ReviewMovieUiState

fun LazyListScope.reviewMovieSection(
    reviews: List<ReviewMovieUiState>,
    interactionListener: MovieDetailsInteractionListener
) {
    if (reviews.isEmpty()) {
        item { EmptyStateText(stringResource(com.amsterdam.ui.R.string.there_is_no_reviews)) }
    } else {
        items(reviews) {item ->
            ReviewCard(
                review = item,
                onToggleExpansion = { interactionListener.onReviewExpansionToggled(item.username) })
        }
    }
}