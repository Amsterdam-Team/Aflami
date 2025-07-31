package com.amsterdam.ui.components.details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.screens.movieDetails.components.PageIndicator


@Composable
fun DetailsPostersPager(
    pagerState: PagerState,
    postersUrl: List<String>,
) {
    Box {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            SafeImageView(
                model = postersUrl[page],
                contentDescription = "",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .animateContentSize(),
                onLoading = { ImageLoadingIndicator() },
                onError = { ImageErrorIndicator() },
            )
        }

        if (postersUrl.size > 1) {
            PageIndicator(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(4.dp)
                    .background(
                        AppTheme.color.primaryVariant,
                        RoundedCornerShape(100.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 2.dp)
                    .align(Alignment.BottomEnd),
                numberOfPages = if (postersUrl.size >= 10) 10
                else postersUrl.size,
                selectedPage = pagerState.currentPage % 10
            )
        }
    }
}