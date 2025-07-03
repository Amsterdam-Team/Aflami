package com.example.designsystem.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun EpisodeCard(
    episodeBanner: Painter,
    episodeRate: Double,
    episodeNumber: Int,
    episodeTitle: String,
    episodeTime: Int,
    publishedAt: String,
    episodeDescription: String,
    modifier: Modifier = Modifier,
    onPlayEpisodeClick: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            modifier
                .fillMaxWidth()
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
    episodeBanner: Painter,
    episodeRate: Double,
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
        Image(
            painter = episodeBanner,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 12.dp,
                            bottomEnd = 4.dp,
                            bottomStart = 12.dp,
                        ),
                    ).border(
                        width = 1.dp,
                        color = AppTheme.color.stroke,
                        shape =
                            RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 12.dp,
                                bottomEnd = 4.dp,
                                bottomStart = 12.dp,
                            ),
                    ).background(AppTheme.color.primaryVariant)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_filled_star),
                contentDescription = null,
                tint = AppTheme.color.yellowAccent,
            )

            Text(
                text = episodeRate.toString(),
                color = AppTheme.color.body,
                style = AppTheme.textStyle.label.small,
            )
        }
    }
}

@Composable
private fun EpisodeInfo(
    episodeNumber: Int,
    episodeTitle: String,
    episodeTime: Int,
    publishedAt: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.padding(vertical = 9.dp),
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
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$episodeTime m",
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
        onClick = onPlayEpisodeClick,
        colors =
            IconButtonDefaults.iconButtonColors(
                containerColor = AppTheme.color.surfaceHigh,
                contentColor = AppTheme.color.primary,
            ),
        modifier =
            Modifier
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = CircleShape,
                ).size(40.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
            contentDescription = null,
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun PreviewEpisodeCard() {
    AflamiTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(AppTheme.color.surface),
        ) {
            EpisodeCard(
                episodeBanner = painterResource(id = R.drawable.bg_man_with_popcorn),
                episodeRate = 4.5,
                episodeNumber = 1,
                episodeTitle = "Recovering a body",
                episodeTime = 58,
                publishedAt = "3 Sep 2020",
                episodeDescription = "In 1935, corrections officer Paul Edgecomb oversees ",
                onPlayEpisodeClick = { },
            )
        }
    }
}
