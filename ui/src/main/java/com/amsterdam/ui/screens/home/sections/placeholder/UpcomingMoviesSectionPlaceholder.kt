package com.amsterdam.ui.screens.home.sections.placeholder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AppTheme
import com.example.designsystem.components.RoundedShimmerPlaceholder

fun LazyListScope.upcomingMoviesSectionPlaceholder() {
    stickyHeader {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RoundedShimmerPlaceholder(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
                height = 30.dp,
                width = 166.dp,
                cornerRadius = 8.dp
            )

            LazyRow(
                modifier = Modifier.background(AppTheme.color.surface),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(10) {
                    MovieGenrePlaceholderItem()
                }
            }
        }
    }

    items(3) {
        RoundedShimmerPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            height = 196.dp,
            width = 166.dp,
            cornerRadius = 16.dp
        )
    }

}

@Composable
private fun MovieGenrePlaceholderItem() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoundedShimmerPlaceholder(
            height = 56.dp,
            width = 56.dp,
            cornerRadius = 12.dp
        )

        RoundedShimmerPlaceholder(
            height = 32.dp,
            width = 40.dp,
            cornerRadius = 8.dp
        )
    }
}