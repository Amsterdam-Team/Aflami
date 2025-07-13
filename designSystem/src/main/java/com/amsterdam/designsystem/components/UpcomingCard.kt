package com.amsterdam.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun UpcomingCard(
    movieImage: String,
    movieTitle: String,
    movieType: String,
    movieYear: String,
    modifier: Modifier = Modifier.size(328.dp, 196.dp),
    movieRating: String? = null,
    movieContentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    BaseCard(
        modifier = modifier,
        movieImage = movieImage,
        movieContentDescription = movieContentDescription,
        movieTitle = movieTitle,
        movieType = movieType,
        movieYear = movieYear,
        movieRating = movieRating,
        contentScale = ContentScale.Crop,
        onClick = onClick
    )
}


@ThemeAndLocalePreviews
@Composable
private fun UpcomingCardPreview() {
    AflamiTheme {
        UpcomingCard(
            movieImage = "",
            movieType = "TV show",
            movieYear = "2016",
            movieTitle = "Your Name",
            movieRating = "9.9")
    }
}
