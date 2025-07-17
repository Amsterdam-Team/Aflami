package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AppTheme

@Composable
fun MovieInfoSection(
    releaseDate: String,
    movieLength: String,
    originCountry: String,
    modifier: Modifier = Modifier
) {
    val items = listOf(releaseDate, movieLength, originCountry)

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEachIndexed { index, item ->
            Text(
                text = item,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.hint
            )

            if (index < items.lastIndex) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(AppTheme.color.stroke, shape = CircleShape)
                )
            }
        }
    }
}