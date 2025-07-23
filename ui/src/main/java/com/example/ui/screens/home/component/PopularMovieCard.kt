package com.example.ui.screens.home.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import com.example.designsystem.components.Icon
import com.example.designsystem.components.ImageErrorIndicator
import com.example.designsystem.components.ImageLoadingIndicator
import com.example.designsystem.components.RatingChip
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.example.imageviewer.ui.SafeImageView
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.components.Text
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.viewmodel.home.HomeUiState.PopularMovieItemUiState

@Composable
fun PopularMovieCard(
    popularMovie: PopularMovieItemUiState,
    ratingAlpha: Float,
    imageWidth : Dp,
    imageHeight : Dp ,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier,horizontalAlignment = Alignment.CenterHorizontally) {
        Box() {
            Box (
                Modifier.height(300.dp),
                contentAlignment = Alignment
                    .BottomCenter
            ) {
                SafeImageView(
                    model = popularMovie.posterUrl,
                    contentDescription = "",
                    modifier =
                        Modifier
                            .size(imageWidth,imageHeight).clip(RoundedCornerShape(24.dp)),
                    onLoading = { ImageLoadingIndicator() },
                    onError = { ImageErrorIndicator() },
                )
            }
            RatingChip(
                popularMovie.rating,
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
            text = popularMovie.name,
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
    val dummyMovie = PopularMovieItemUiState(
        name = "Inception",
        posterUrl = "https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg",
        rating = "8.8"
    )

    AflamiTheme {
        PopularMovieCard(
            popularMovie = dummyMovie,
            modifier = Modifier
                .width(244.dp)
                .height(300.dp),
            ratingAlpha = 1f,
            imageWidth = 244.dp,
            imageHeight = 300.dp
        )
    }
}
