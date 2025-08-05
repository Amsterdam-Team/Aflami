package com.amsterdam.ui.screens.myRating.placeholders

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.RoundedShimmerPlaceholder

fun LazyGridScope.mediaCardsPlaceholder(modifier: Modifier = Modifier) {
    items(10) {
            RoundedShimmerPlaceholder(
                modifier = modifier.fillMaxWidth(),
                height = 222.dp,
                width = 160.dp,
                cornerRadius = 16.dp
            )
        }
}