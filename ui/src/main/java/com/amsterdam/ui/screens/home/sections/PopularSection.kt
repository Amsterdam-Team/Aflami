package com.amsterdam.ui.screens.home.sections

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import com.amsterdam.designsystem.components.SectionTitle
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.ui.R
import com.amsterdam.ui.components.CategoryChip
import com.amsterdam.ui.screens.home.component.PopularMediaItemCard
import com.amsterdam.ui.screens.home.sections.placeholder.popularSectionPlaceholder
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreLabel
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getTvShowGenreLabel
import com.amsterdam.viewmodel.home.HomeUiState
import com.amsterdam.viewmodel.home.HomeUiState.PopularMediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@SuppressLint("RestrictedApi", "ConfigurationScreenWidthHeight", "UnusedBoxWithConstraintsScope")
fun LazyListScope.popularSection(
    state: HomeUiState.PopularMediaSectionUiState,
    onClickMediaItem: (Long, MediaType) -> Unit,
    isVisible: Boolean
) {
    if (isVisible) {
        if (state.isLoading) {
            popularSectionPlaceholder()
        }
        else if(state.mediaItems.isEmpty())
            return
        else {
            item {
                Box {
                    val pagerState = rememberPagerState(
                        initialPage = Int.MAX_VALUE / 2,
                        pageCount = { Int.MAX_VALUE }
                    )

                    Crossfade (
                        targetState = pagerState.currentPage,
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    ) { page ->
                        BlurredMediaPoster(
                            posterUrl = state.mediaItems[page % state.mediaItems.size].posterUrl,
                            modifier = Modifier.offset(y = (-26).dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(top = 56.dp)
                    ) {
                        SectionTitle(
                            title = stringResource(R.string.popular),
                            icon = painterResource(com.amsterdam.designsystem.R.drawable.ic_fire),
                            tintColor = AppTheme.color.secondary,
                            modifier = Modifier
                                .zIndex(1f)
                                .padding(bottom = 12.dp)
                        )
                        AutoScrollingPager(pagerState)
                        BoxWithConstraints {
                            val screenWidth = maxWidth
                            PopularMediaPager(
                                pagerState = pagerState,
                                state = state,
                                screenWidth = screenWidth,
                                onClickMediaItem = onClickMediaItem
                            )
                        }

                        Crossfade(
                            targetState = pagerState.currentPage,
                            animationSpec = tween(500, easing = FastOutSlowInEasing)
                        ) { page ->
                            DisplayMediaGenres(
                                mediaItem = state.mediaItems[page % state.mediaItems.size]
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayMediaGenres(
    mediaItem: PopularMediaItemUiState,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(mediaItem.categories) { category ->
            if (mediaItem.type == MediaType.MOVIE) {
                MovieGenre.entries.find { it.name.equals(category, ignoreCase = true) }
                    ?.let { genre ->
                        CategoryChip(categoryName = getMovieGenreLabel(genre))
                    }
            } else {
                TvShowGenre.entries.find { it.name.equals(category, ignoreCase = true) }
                    ?.let { genre ->
                        CategoryChip(categoryName = getTvShowGenreLabel(genre))
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
    val interactionSource = pagerState.interactionSource
    var isUserInteracting by remember { mutableStateOf(false) }
    LaunchedEffect(pagerState) {
        interactionSource.interactions.collect { interaction ->
            isUserInteracting = when (interaction) {
                is PressInteraction.Press,
                is DragInteraction.Start -> true
                else -> false
            }
        }
    }
    LaunchedEffect(isUserInteracting) {
        while (!isUserInteracting) {
            delay(intervalMillis)
            val nextPage = (pagerState.currentPage + 1) % Int.MAX_VALUE
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
        }
    }
}

@Composable
private fun PopularMediaPager(
    pagerState: PagerState,
    state: HomeUiState.PopularMediaSectionUiState,
    screenWidth: Dp,
    modifier: Modifier = Modifier,
    onClickMediaItem: (Long, MediaType) -> Unit
){
    val itemWidth = 207.dp
    val horizontalPadding = (screenWidth - itemWidth) / 2
    HorizontalPager(
        state = pagerState,
        pageSpacing = 16.dp,
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        modifier = modifier
    ) { page ->
        val currentPageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

        val width by animateDpAsState(
            targetValue = lerp(
                207.dp,
                244.dp,
                1f - currentPageOffset.coerceIn(0f, 1f)
            ),
            label = "width"
        )

        val height by animateDpAsState(
            targetValue = lerp(
                276.dp, 300.dp, 1f - currentPageOffset.coerceIn(0f, 1f)
            ), label = "height"
        )
        val rateAlpha by animateFloatAsState(
            targetValue = androidx.compose.ui.util.lerp(
                0f,
                1f,
                1f - currentPageOffset.coerceIn(0f, 1f)
            ),
            label = "height"
        )

        PopularMediaItemCard(
            popularMediaItem = state.mediaItems[page % state.mediaItems.size],
            ratingAlpha = rateAlpha,
            imageWidth = width,
            imageHeight = height,
            onClickMediaItem = onClickMediaItem
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun PopularSectionPreview() {
    val dummyMovies = List(5) {
        PopularMediaItemUiState(
            name = "Movie $it",
            posterUrl = "https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg",
            rating = (8.5f + it).toString(),
            categories = listOf(
                MovieGenre.entries.random().name,
                MovieGenre.entries.random().name,
                MovieGenre.entries.random().name
            )
        )
    }

    AflamiTheme {
        LazyColumn {
            popularSection(
                state = HomeUiState.PopularMediaSectionUiState(
                    mediaItems = dummyMovies
                ),
                onClickMediaItem = { _, _ -> },
                isVisible = true
            )
        }
    }
}