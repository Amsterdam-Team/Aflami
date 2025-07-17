package com.example.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun UpcomingCard(
    movieImage: @Composable () -> Unit,
    movieTitle: String,
    movieType: String,
    movieYear: String,
    modifier: Modifier = Modifier,
    movieRating: String? = null,
    onClick: () -> Unit = {},
) {
    BaseCard(
        modifier = modifier.size(328.dp, 196.dp),
        movieImage = movieImage,
        movieTitle = movieTitle,
        movieType = movieType,
        movieYear = movieYear,
        movieRating = movieRating,
        onClick = onClick,
    )
}

@ThemeAndLocalePreviews
@Composable
private fun UpcomingCardPreview() {
    AflamiTheme {
        UpcomingCard(
            movieImage = {},
            movieType = "TV show",
            movieYear = "2016",
            movieTitle = "Your Name",
            movieRating = "9.9",
        )
    }
}
