package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.viewmodel.shared.movieAndSeriseDetails.ReviewUiState

fun LazyListScope.ReviewSection(reviews: List<ReviewUiState>) {
    if (reviews.isEmpty())
        item {
            EmptyStateText(stringResource(com.example.ui.R.string.there_is_no_reviews))
        }
    else
        itemsIndexed(reviews) { index, item ->
            val topPadding = if (index == 0) 0 else 12
            ReviewCard(item, modifier = Modifier.padding(top = topPadding.dp))
        }
}