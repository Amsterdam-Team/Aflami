package com.amsterdam.ui.screens.home.sections.placeholder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.RoundedShimmerPlaceholder

fun LazyListScope.movieSectionPlaceholder(modifier: Modifier = Modifier) {
    item {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RoundedShimmerPlaceholder(
                height = 30.dp,
                width = 166.dp,
                cornerRadius = 8.dp
            )

            RoundedShimmerPlaceholder(
                height = 24.dp,
                width = 46.dp,
                cornerRadius = 8.dp
            )
        }
    }

    item {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            userScrollEnabled = false,
        ) {
            items(10) {
                RoundedShimmerPlaceholder(
                    height = 222.dp,
                    width = 156.dp,
                    cornerRadius = 8.dp
                )
            }

        }
    }
}