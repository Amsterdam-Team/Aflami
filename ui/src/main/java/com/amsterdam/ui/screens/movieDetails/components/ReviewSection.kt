package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.res.stringResource
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState

fun LazyListScope.ReviewSection(reviews: List<ReviewUiState>) {
    if (reviews.isEmpty())
        item {
            EmptyStateText(stringResource(com.amsterdam.ui.R.string.there_is_no_reviews))
        }
    else
        itemsIndexed(reviews) { index, item ->
            ReviewCard(item)
        }
}