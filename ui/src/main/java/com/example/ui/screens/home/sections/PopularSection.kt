package com.example.ui.screens.home.sections

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import com.amsterdam.blurred.blurProcessor.BlurEdgeTreatment
import com.amsterdam.blurred.ui.modifier.blur
import com.example.designsystem.R
import com.example.designsystem.components.SectionTitle
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.screens.home.component.PopularMovieCard
import com.example.viewmodel.home.HomeUiState.PopularMovieItemUiState
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@SuppressLint("RestrictedApi", "ConfigurationScreenWidthHeight", "UnusedBoxWithConstraintsScope")
fun LazyListScope.popularSection(popularMovies: List<PopularMovieItemUiState>, isVisible: Boolean) {
    val pagerState = PagerState(currentPage = Int.MAX_VALUE / 2) { Int.MAX_VALUE }

    item {
        AnimatedSectionVisibility(visible = isVisible) {
            SectionTitle(
                title = stringResource(R.string.popular),
                icon = painterResource(R.drawable.ic_fire),
                tintColor = AppTheme.color.secondary,
                modifier = Modifier
                    .zIndex(1f)
                    .padding(bottom = 12.dp)
            )
        }
    }

    item {
        AnimatedSectionVisibility(visible = isVisible) {
            AutoScrollingPager(pagerState = pagerState)
        }
    }

    item {
        AnimatedSectionVisibility(visible = isVisible) {
            BoxWithConstraints {
                SafeImageView(
                    model = popularMovies[pagerState.currentPage % popularMovies.size].posterUrl,
                    contentDescription = popularMovies[pagerState.currentPage % popularMovies.size].name,
                    onLoading = { },
                    onError = { },
                    modifier = Modifier
                        .fillMaxSize()
                        .requiredHeight(470.dp)
                        .offset(y = -200.dp)
                        .blur(10f, edgeTreatment = BlurEdgeTreatment.UNBOUNDED)
                )

                val screenWidth = maxWidth
                val itemWidth = 207.dp
                val horizontalPadding = (screenWidth - itemWidth) / 2
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 16.dp,
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) { page ->
                    val currentPageOffset = (
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            ).absoluteValue

                    val width by animateDpAsState(
                        targetValue = lerp(207.dp, 244.dp, 1f - currentPageOffset.coerceIn(0f, 1f)),
                        label = "width"
                    )

                    val height by animateDpAsState(
                        targetValue = lerp(276.dp, 300.dp, 1f - currentPageOffset.coerceIn(0f, 1f)),
                        label = "height"
                    )
                    PopularMovieCard(
                        popularMovie = popularMovies[page % popularMovies.size],
                        modifier = Modifier.size(width, height)
                    )
                }
            }
        }
    }
}

@Composable
private fun AutoScrollingPager(
    pagerState: PagerState,
    intervalMillis: Long = 4000L
) {
    LaunchedEffect(pagerState) {
        while (true) {
            delay(intervalMillis)
            val nextPage = (pagerState.currentPage + 1) % Int.MAX_VALUE
            pagerState.animateScrollToPage(nextPage)
        }
    }
}


@ThemeAndLocalePreviews
@Composable
private fun PopularSectionPreview() {
    val dummyMovies = List(5) {
        PopularMovieItemUiState(
            name = "Movie $it",
            posterUrl = "https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg",
            rating = (8.5f + it).toString()
        )
    }

    AflamiTheme {
        LazyColumn {
            popularSection(
                popularMovies = dummyMovies,
                isVisible = true
            )
        }
    }
}