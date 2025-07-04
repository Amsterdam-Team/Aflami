package com.example.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import kotlin.math.absoluteValue

@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    aflamiImageList: List<Int>,
) {
    val pagerState = rememberPagerState { aflamiImageList.size }
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 46.dp),
        pageSpacing = 16.dp,
        modifier = Modifier.fillMaxWidth()
    ) { index ->
        val pageOffset =
            (pagerState.currentPage - index).toFloat() + pagerState.currentPageOffsetFraction
        val scaleFactor = lerp(
            start = 0.9f,
            stop = 1f,
            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
        )
        Box {
            Image(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    .border(
                        width = 1.dp,
                        color = AppTheme.color.stroke,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .aspectRatio(0.81f)
                    .clip(RoundedCornerShape(24.dp)),
                painter = painterResource(id = aflamiImageList[index]),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            b(
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .align(Alignment.TopEnd)
            )
            b(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
            )

        }
    }
}

@Composable
private fun b(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(AppTheme.color.primary)
    ) {

    }
}


@Composable
@ThemeAndLocalePreviews
private fun CustomSliserPreview() {
    AflamiTheme {
        CustomSlider(
            aflamiImageList = listOf(
                R.drawable.bg_man_with_popcorn,
                R.drawable.bg_directly_shot_film,
                R.drawable.bg_cinema_movie_theater,
                R.drawable.bg_directly_shot_film,
                R.drawable.bg_children_wearing_3d,
                R.drawable.bg_children_wearing_3d,
            )
        )
    }
}