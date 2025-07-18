package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun MovieCard(
    movieImage: @Composable () -> Unit,
    movieTitle: String,
    movieType: String,
    movieYear: String,
    modifier: Modifier = Modifier,
    movieRating: String? = null,
    topIcon: Painter? = null,
    onClick: () -> Unit = {},
) {
    BaseCard(
        modifier = modifier.size(156.dp, 222.dp),
        movieImage = movieImage,
        movieTitle = movieTitle,
        movieType = movieType,
        movieYear = movieYear,
        movieRating = movieRating,
        onClick = onClick,
        topIcon = topIcon,
    )
}

@ThemeAndLocalePreviews
@Composable
private fun MovieCardPreview() {
    AflamiTheme {
        MovieCard(
            movieImage = { },
            movieType = "TV show",
            movieYear = "2016",
            movieTitle = "Your Name",
            movieRating = "9.9",
            topIcon = painterResource(R.drawable.img_user_rating),
        )
    }
}
