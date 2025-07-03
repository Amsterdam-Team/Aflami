package com.example.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun UpcomingCard(
    movieImage: Painter,
    movieTitle: String,
    movieType: String,
    movieYear: String,
    movieRating: String? = null,
    modifier: Modifier = Modifier.size(328.dp, 196.dp),
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
            movieImage = painterResource(R.drawable.u),
            movieType = "TV show",
            movieYear = "2016",
            movieTitle = "Your Name",
            movieRating = "9.9")
    }
}
