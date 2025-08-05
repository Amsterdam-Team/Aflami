package com.amsterdam.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun MediaCard(
    movieImage: @Composable () -> Unit,
    movieTitle: String,
    movieType: String,
    movieYear: String,
    modifier: Modifier = Modifier,
    movieRating: String? = null,
    topIcon: Painter? = null,
    onTopIconClick: () -> Unit = {},
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
        onTopIconClick = onTopIconClick,
    )
}

@ThemeAndLocalePreviews
@Composable
private fun MovieCardPreview() {
    AflamiTheme {
        MediaCard(
            movieImage = { },
            movieType = "TV show",
            movieYear = "2016",
            movieTitle = "Your Name",
            movieRating = "9.9",
            topIcon = painterResource(R.drawable.img_user_rating),
        )
    }
}
