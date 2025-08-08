package com.amsterdam.ui.screens.seriesDetails.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.screens.movieDetails.components.EmptyStateText
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsInteractionListener
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.ReviewTvShowUiState

fun LazyListScope.reviewTvShowSection(
    reviews: List<ReviewTvShowUiState>,
    interactionListener: SeriesDetailsInteractionListener
) {
    if (reviews.isEmpty()) {
        item { EmptyStateText(stringResource(com.amsterdam.ui.R.string.there_is_no_reviews)) }
    } else {
        items(reviews) {item ->
            ReviewTvShowCard(
                review = item,
                onToggleExpansion = { interactionListener.onReviewExpansionToggled(item.username) })
        }
    }
}