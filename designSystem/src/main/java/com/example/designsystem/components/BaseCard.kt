package com.example.designsystem.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun BaseCard(
    movieImage: @Composable () -> Unit,
    movieTitle: String,
    movieType: String,
    movieYear: String,
    modifier: Modifier = Modifier,
    movieRating: String? = null,
    topIcon: Painter? = null,
    onClick: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .size(328.dp, 196.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(width = 1.dp, color = AppTheme.color.stroke, shape = RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onClick,
                ),
    ) {
        movieImage()
        movieRating?.let {
            RatingChip(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp),
                rating = it,
            )
        }
        topIcon?.let { IconContainer(it) }
        MovieInfoSection(movieTitle, movieType, movieYear)
        GradientOverlay()
    }
}

@Composable
private fun BoxScope.MovieInfoSection(
    movieTitle: String,
    movieType: String,
    movieYear: String,
) {
    Column(
        modifier =
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .zIndex(2f)
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom =
                        8.dp,
                ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text =
            movieTitle,
            maxLines = 1,
            style = AppTheme.textStyle.label.large,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.color.onPrimary,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = movieType,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.onPrimaryBody,
            )
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(AppTheme.color.onPrimaryBody),
            )
            Text(
                text = movieYear,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.onPrimaryBody,
            )
        }
    }
}

@Composable
private fun BoxScope.GradientOverlay() {
    val overlayDarkColor = AppTheme.color.overlayDark
    Box(
        modifier =
            Modifier
                .zIndex(1f)
                .align(Alignment.BottomCenter)
                .height(108.dp)
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush =
                            Brush.verticalGradient(
                                colors = overlayDarkColor,
                            ),
                    )
                },
    )
}

@Composable
private fun BoxScope.IconContainer(topIcon: Painter) {
    Box(
        modifier =
            Modifier
                .align(Alignment.TopStart)
                .padding(start = 4.dp, top = 4.dp)
                .size(32.dp)
                .background(
                    color = AppTheme.color.iconBackGround,
                    RoundedCornerShape(12.dp),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = topIcon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = AppTheme.color.redAccent,
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun BaseCardPreview() {
    AflamiTheme {
        BaseCard(
            movieImage = { },
            movieType = "TV show",
            movieYear = "2016",
            movieTitle = "Your Name",
            movieRating = "9.9",
        )
    }
}
