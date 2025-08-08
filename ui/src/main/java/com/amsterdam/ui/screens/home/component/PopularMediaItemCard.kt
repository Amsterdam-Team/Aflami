package com.amsterdam.ui.screens.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.components.RatingChip
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.home.HomeUiState.PopularMediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MediaType

@Composable
fun PopularMediaItemCard(
    popularMediaItem: PopularMediaItemUiState,
    ratingAlpha: Float,
    imageWidth: Dp,
    imageHeight: Dp,
    onClickMediaItem: (Long, MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
    Column(modifier = modifier,horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Box (
                Modifier
                    .height(300.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onClickMediaItem(popularMediaItem.id, popularMediaItem.type) },
                    ),
                contentAlignment = Alignment
                    .BottomCenter
            ) {
                SafeImageView(
                    model = popularMediaItem.posterUrl,
                    contentDescription = "",
                    modifier =
                        Modifier
                            .size(imageWidth, imageHeight)
                            .clip(RoundedCornerShape(24.dp)),
                    onLoading = { ImageLoadingIndicator() },
                    safetyLevel = safetyLevel,
                    onError = { ImageErrorIndicator() },
                )
            }
            RatingChip(
                popularMediaItem.rating,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .alpha(ratingAlpha)
            )
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color = AppTheme.color.onPrimary, shape = CircleShape)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(R.drawable.ic_play), contentDescription = null)
            }
        }
        Text(
            text = popularMediaItem.name,
            style = AppTheme.textStyle.title.small,
            color = AppTheme.color.title, modifier = Modifier.padding(top = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}


@ThemeAndLocalePreviews
@Composable
private fun PopularMovieCardPreview() {
    val dummyMovie = PopularMediaItemUiState(
        name = "Inception",
        posterUrl = "https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg",
        rating = "8.8"
    )

    AflamiTheme {
        PopularMediaItemCard(
            popularMediaItem = dummyMovie,
            modifier = Modifier
                .width(244.dp)
                .height(300.dp),
            ratingAlpha = 1f,
            imageWidth = 244.dp,
            imageHeight = 300.dp,
            onClickMediaItem = { _, _ ->}
        )
    }
}
