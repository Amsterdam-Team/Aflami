package com.amsterdam.ui.screens.seriesDetails

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.chip.Chip
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.EpisodeCard
import com.amsterdam.ui.components.MustLoginDialog
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.RatingChip
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.components.details.DetailsPostersPager
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.movieDetails.components.CastSection
import com.amsterdam.ui.screens.movieDetails.components.CategoryChip
import com.amsterdam.ui.screens.movieDetails.components.companyProductionSection
import com.amsterdam.ui.screens.movieDetails.components.DescriptionSection
import com.amsterdam.ui.screens.movieDetails.components.EmptyStateText
import com.amsterdam.ui.screens.movieDetails.components.GallerySection
import com.amsterdam.ui.screens.movieDetails.components.moreLikeSection
import com.amsterdam.ui.screens.movieDetails.components.PlayButton
import com.amsterdam.ui.screens.movieDetails.components.reviewSection
import com.amsterdam.ui.screens.movieDetails.getMovieAndSeriesDetailsDialogTitle
import com.amsterdam.ui.screens.movieDetails.getSeriesExtrasSectionItemInfo
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getTvShowGenreLabel
import com.amsterdam.ui.utils.formateAsRate
import com.amsterdam.ui.utils.safeNavigate
import com.amsterdam.viewmodel.cast.MediaType
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsEffect
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsInteractionListener
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.EpisodeUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsViewModel
import com.amsterdam.viewmodel.shared.Selectable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SeriesDetailsScreen(
    viewModel: SeriesDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current
    SeriesDetailsContent(
        state = state,
        interaction = viewModel
    )
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            effect?.let {
                when (it) {
                    SeriesDetailsEffect.NavigateBack -> navController.popBackStack()
                    SeriesDetailsEffect.NavigateToCastScreen -> {
                        navController.safeNavigate(
                            Route.Cast(
                                mediaType = MediaType.TV_SHOW.name,
                                mediaId = state.tvShowId
                            )
                        )
                    }

                    SeriesDetailsEffect.NavigateToLoginScreenEffect -> navController.safeNavigate(
                        Route.Login
                    )

                    is SeriesDetailsEffect.NavigateToMovieDetails -> {
                        navController.safeNavigate(Route.MovieDetails(it.movieId))
                    }
                }
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SeriesDetailsContent(
    state: SeriesDetailsUiState,
    interaction: SeriesDetailsInteractionListener
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp by remember { mutableStateOf(configuration.screenWidthDp.dp) }
    val screenHeightDp = configuration.screenHeightDp.dp
    var seriesExtrasSectionYOffsetDp by remember { mutableStateOf(0.dp) }
    val listState = rememberLazyListState()
    val animationDuration by remember { mutableIntStateOf(1000) }
    val surface = AppTheme.color.surface
    val transparent = AppTheme.color.surface.copy(alpha = 0f)
    val stroke = AppTheme.color.stroke
    val appBarColor by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex != 0 ||
                listState.firstVisibleItemScrollOffset != 0
            ) {
                surface
            } else {
                transparent
            }
        }
    }
    val dividerColor by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex != 0) {
                stroke
            } else {
                transparent
            }
        }
    }
    val pagerState = rememberPagerState { state.postersUrls.size }

    LaunchedEffect(true) {
        while (true) {
            delay(4000)
            pagerState.animateScrollToPage(((pagerState.currentPage + 1) % 10))
        }
    }

    AnimatedVisibility(
        state.isLoading,
        enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration))
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingContainer()
        }
    }

    AnimatedVisibility(
        state.networkError, enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration))
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NoNetworkContainer(
                onClickRetry = interaction::onClickRetryButton,
            )
        }
    }

    AnimatedVisibility(
        modifier = Modifier,
        visible = state.isLoginDialogVisible
    ) {
        MustLoginDialog(
            title = state.dialogType.getMovieAndSeriesDetailsDialogTitle(),
            onDismiss = interaction::onCancelClicked,
            onClickLogin = interaction::onNavigateToLoginClicked,
        )
    }

    AnimatedVisibility(
        !state.isLoading && !state.networkError,
        enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(appBarColor)
            ) {
                DefaultAppBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .statusBarsPadding(),
                    firstOption = painterResource(R.drawable.ic_outlined_star),
                    lastOption = painterResource(R.drawable.ic_outlined_add_to_favourite),
                    onNavigateBackClicked = interaction::onNavigateBack,
                    onFirstOptionClicked = interaction::onRateClicked,
                    onLastOptionClicked = interaction::onAddToListClicked
                )
                HorizontalDivider(color = dividerColor)
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.color.surface)
                    .navigationBarsPadding()
                    .animateContentSize()
            ) {

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(263.dp)
                    ) {
                        if (state.postersUrls.isEmpty()) {
                            ImageErrorIndicator()
                        } else {
                            DetailsPostersPager(
                                pagerState = pagerState,
                                postersUrl = state.postersUrls
                            )
                        }

                        RatingChip(
                            state.rating.formateAsRate(),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(vertical = 4.dp)
                                .padding(start = 4.dp)
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.color.surface)
                    ) {

                        PlayButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .offset(y = (-32).dp),
                            isActive = state.hasVideo
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-20).dp)
                        ) {

                            Text(
                                text = state.title,
                                style = AppTheme.textStyle.title.large,
                                color = AppTheme.color.title,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            if(state.categories.isNotEmpty()){
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    items(state.categories) {
                                        CategoryChip(categoryName = getTvShowGenreLabel(it))
                                    }
                                }
                            }

                            SeriesInfoSection(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .padding(horizontal = 16.dp),
                                airDate = state.airDate,
                                seasonCount = state.seasonCount,
                                originCountry = state.originCountry,
                            )

                            DescriptionSection(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .padding(horizontal = 16.dp),
                                description = state.description
                            )

                            CastSection(
                                modifier = Modifier.padding(top = 24.dp),
                                actors = state.cast,
                                onClickAllCast = interaction::onClickShowAllCast
                            )
                            Spacer(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .requiredWidth(screenWidthDp)
                                    .height(1.dp)
                                    .background(AppTheme.color.stroke)
                            )
                            SeriesExtrasSection(
                                modifier = Modifier.padding(top = 12.dp)
                                    .onGloballyPositioned { coordinates ->
                                        seriesExtrasSectionYOffsetDp =
                                            coordinates.positionOnScreen().y.dp
                                    },
                                extras = state.extraItem,
                                onClickExtras = interaction::onClickSeriesExtraItem
                            )
                        }

                    }
                }
                state.extraItem
                    .find { it.isSelected }
                    ?.item
                    ?.let { selectedExtra ->
                        when (selectedExtra) {
                            SeriesExtras.SEASONS -> {
                                    seasonsSection(seasons = state.seasons, interaction = interaction)
                            }
                            SeriesExtras.MORE_LIKE_THIS -> moreLikeSection(
                                similarMovies = state.similarSeries,
                                onClick = { movieId ->
                                    interaction.onClickSimilarMovie(movieId)
                                }
                            )
                            SeriesExtras.REVIEWS -> reviewSection(state.reviews)
                            SeriesExtras.GALLERY -> item {
                                GallerySection(gallery = state.gallery)
                            }
                            SeriesExtras.COMPANY_PRODUCTION -> companyProductionSection(
                                state.productionCompanies
                            )
                        }
                    }

                item {
                    val lastVisibleItemInfo by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull() } }
                    val totalItemsCount by remember { derivedStateOf { listState.layoutInfo.totalItemsCount } }
                    val spacerHeight: Dp by remember {
                        derivedStateOf {
                            if (seriesExtrasSectionYOffsetDp > 0.dp || (totalItemsCount > 0 && lastVisibleItemInfo?.index == totalItemsCount - 1)){
                                screenHeightDp
                            } else {
                                0.dp
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(spacerHeight))
                }
            }
        }
    }
}

@Composable
private fun SeriesInfoSection(
    modifier: Modifier = Modifier,
    airDate: String,
    seasonCount: String,
    originCountry: String
) {
    val items = listOf(airDate, seasonCount, originCountry)

    if (!items.all{it.isEmpty()}) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEachIndexed { index, item ->
                AnimatedVisibility(
                    visible = item.isNotEmpty(),
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    Text(
                        text = item,
                        style = AppTheme.textStyle.label.small,
                        color = AppTheme.color.hint
                    )

                    if (index < items.lastIndex) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(AppTheme.color.stroke, shape = CircleShape)
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun SeriesExtrasSection(
    modifier: Modifier = Modifier,
    extras: List<Selectable<SeriesExtras>>,
    onClickExtras: (SeriesExtras) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(items = extras) {
            val extrasSectionItemInfo = it.item.getSeriesExtrasSectionItemInfo()
            Chip(
                modifier = Modifier.size(70.dp, 96.dp),
                icon = painterResource(extrasSectionItemInfo.iconResId),
                label = stringResource(extrasSectionItemInfo.textResId),
                isSelected = it.isSelected,
                onClick = { onClickExtras(it.item) }
            )
        }
    }
}

private fun LazyListScope.seasonsSection(
    seasons: List<SeasonUiState>,
    interaction: SeriesDetailsInteractionListener
) {
    if (seasons.isEmpty()) {
        item { EmptyStateText(stringResource(R.string.there_is_no_seasons)) }
    } else {
        seasons.forEachIndexed { index, season ->
            stickyHeader {
                SeasonHeader(
                    season = season,
                    onClickSeasonMenu = { seasonNumber ->
                        interaction.onClickSeasonMenu(seasonNumber)
                    }
                )
            }
            val episodes = if (season.isExpanded) season.episodes else emptyList()
            items(episodes, key = { "${it.id}-${season.episodes.indexOf(it)}-${index}" }) {
                EpisodesMenu(it)
            }
        }
    }
}

@Composable
private fun SeasonHeader(
    season: SeasonUiState,
    onClickSeasonMenu: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClickSeasonMenu(season.seasonNumber) }
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = season.title,
                color = AppTheme.color.title,
                style = AppTheme.textStyle.title.small,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${season.episodeCount} ${stringResource(R.string.episodes)}",
                color = AppTheme.color.hint,
                style = AppTheme.textStyle.label.small,
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                modifier = Modifier
                    .size(20.dp),
                painter = if (season.isExpanded) painterResource(R.drawable.ic_arrow_up) else
                    painterResource(R.drawable.ic_arrow_down),
                contentDescription = null,
                tint = AppTheme.color.title,
            )
        }
        HorizontalDivider(color = AppTheme.color.stroke)
    }
}

@Composable
private fun EpisodesMenu(
    episode: EpisodeUiState,
) {
    EpisodeCard(
        episodeBanner = episode.imageUrl,
        episodeRate = episode.rating,
        episodeNumber = episode.number,
        episodeTitle = episode.title,
        episodeTime = episode.duration,
        publishedAt = episode.airDate,
        episodeDescription = episode.description,
        modifier = Modifier.padding(vertical = 12.dp),
        onPlayEpisodeClick = { }
    )
}

@Composable
@ThemeAndLocalePreviews
private fun SeriesDetailsContentPreview() {
    AflamiTheme {
        SeriesDetailsContent(
            state = SeriesDetailsUiState(),
            interaction = object : SeriesDetailsInteractionListener {
                override fun onClickSeriesExtraItem(seriesExtras: SeriesExtras) {}
                override fun onNavigateBack() {}
                override fun onClickRetryButton() {}
                override fun onClickShowAllCast() {}
                override fun onAddToListClicked() {}
                override fun onRateClicked() {}
                override fun onClickSeasonMenu(seasonNumber: Int) {}
                override fun onNavigateToLoginClicked() {}
                override fun onCancelClicked() {}
                override fun onClickSimilarMovie(movieId: Long) {}
            }
        )
    }
}