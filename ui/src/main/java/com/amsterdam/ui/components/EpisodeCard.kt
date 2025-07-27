package com.amsterdam.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView

@Composable
fun EpisodeCard(
    episodeBanner: String,
    episodeRate: String,
    episodeNumber: Int,
    episodeTitle: String,
    episodeTime: String,
    publishedAt: String,
    episodeDescription: String,
    modifier: Modifier = Modifier,
    onPlayEpisodeClick: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            modifier
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .animateContentSize(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                EpisodeBanner(
                    episodeBanner = episodeBanner,
                    episodeRate = episodeRate,
                )
                EpisodeInfo(
                    episodeNumber = episodeNumber,
                    episodeTitle = episodeTitle,
                    episodeTime = episodeTime,
                    publishedAt = publishedAt,
                )
            }
            PlayEpisodeButton(onPlayEpisodeClick)
        }
        EpisodeDescription(
            episodeDescription = episodeDescription,
        )
    }
}

@Composable
private fun EpisodeBanner(
    episodeBanner: String,
    episodeRate: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .size(116.dp, 78.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(12.dp),
                ),
    ) {
        SafeImageView(
            model = episodeBanner,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onLoading = { ImageLoadingIndicator() },
            onError = { ImageErrorIndicator() },
        )
        RatingChip(
            rating = episodeRate.toString(),
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp),
        )
    }
}

@Composable
private fun EpisodeInfo(
    episodeNumber: Int,
    episodeTitle: String,
    episodeTime: String,
    publishedAt: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .padding(vertical = 9.dp)
    ) {
        Text(
            text = stringResource(R.string.episode, episodeNumber),
            color = AppTheme.color.title,
            style = AppTheme.textStyle.label.large,
        )

        Text(
            text = episodeTitle,
            color = AppTheme.color.hint,
            style = AppTheme.textStyle.label.small,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = episodeTime,
                color = AppTheme.color.hint,
                style = AppTheme.textStyle.label.small,
            )

            Box(
                modifier =
                    Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(color = AppTheme.color.stroke),
            )

            Text(
                text = publishedAt,
                color = AppTheme.color.hint,
                style = AppTheme.textStyle.label.small,
            )
        }
    }
}

@Composable
private fun EpisodeDescription(episodeDescription: String) {
    var showFullDescription by remember { mutableStateOf(false) }
    var isTextTruncated by remember { mutableStateOf(false) }

    Text(
        text = episodeDescription,
        color = AppTheme.color.hint,
        style = AppTheme.textStyle.label.small,
        maxLines = if (showFullDescription) Int.MAX_VALUE else 2,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            isTextTruncated = textLayoutResult.hasVisualOverflow
        },
        modifier =
            Modifier
                .clickable(
                    interactionSource = null,
                    indication = null,
                    enabled = if (showFullDescription) true else isTextTruncated,
                    onClick = { showFullDescription = !showFullDescription },
                ),
    )
}

@Composable
private fun PlayEpisodeButton(onPlayEpisodeClick: () -> Unit) {
    IconButton(
        painter = painterResource(R.drawable.ic_play),
        contentDescription = null,
        onClick = onPlayEpisodeClick,
        containerColor = AppTheme.color.surfaceHigh,
        tint = AppTheme.color.primary,
        modifier =
            Modifier
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = CircleShape,
                )
                .size(40.dp),
        shape = CircleShape
    )
}

@ThemeAndLocalePreviews
@Composable
private fun EpisodeCardPreview() {
    AflamiTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(AppTheme.color.surface),
        ) {
            EpisodeCard(
                episodeBanner = "",
                episodeRate = "4.5",
                episodeNumber = 1,
                episodeTitle = "Recovering a body",
                episodeTime = "58",
                publishedAt = "3 Sep 2020",
                episodeDescription = "In 1935, corrections officer Paul Edgecomb oversees ",
                onPlayEpisodeClick = { },
            )
        }
    }
}