package com.amsterdam.ui.screens.seriesDetails.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.R
import com.amsterdam.ui.components.movieAndTvShowDetails.ReviewCard
import com.amsterdam.ui.components.EmptyStateText
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsInteractionListener
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.ReviewTvShowUiState

fun LazyListScope.reviewTvShowSection(
    reviews: List<ReviewTvShowUiState>,
    interactionListener: SeriesDetailsInteractionListener
) {
    if (reviews.isEmpty()) {
        item { EmptyStateText(stringResource(R.string.there_is_no_reviews)) }
    } else {
        items(reviews) {item ->
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