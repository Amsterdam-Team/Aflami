package com.amsterdam.ui.screens.seriesDetails

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.chip.Chip
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
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
import com.amsterdam.ui.navigation.Route.*
import com.amsterdam.ui.screens.movieDetails.components.CastSection
import com.amsterdam.ui.screens.movieDetails.components.CategoryChip
import com.amsterdam.ui.screens.movieDetails.components.DescriptionSection
import com.amsterdam.ui.screens.movieDetails.components.EmptyStateText
import com.amsterdam.ui.screens.movieDetails.components.PlayButton
import com.amsterdam.ui.screens.movieDetails.components.RateDialog
import com.amsterdam.ui.screens.movieDetails.components.companyProductionSection
import com.amsterdam.ui.screens.movieDetails.components.gallerySection
import com.amsterdam.ui.screens.movieDetails.components.moreLikeSection
import com.amsterdam.ui.screens.movieDetails.getMovieAndSeriesDetailsDialogTitle
import com.amsterdam.ui.screens.movieDetails.getSeriesExtrasSectionItemInfo
import com.amsterdam.ui.screens.openYouTubeVideo
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getTvShowGenreLabel
import com.amsterdam.ui.screens.seriesDetails.component.reviewSection
import com.amsterdam.viewmodel.cast.MediaType
import com.amsterdam.viewmodel.myRating.RateDialogInteractionListener
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun SeriesDetailsScreen(
    viewModel: SeriesDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current
    val context = LocalContext.current
    val successRateMessage = stringResource(R.string.your_rating_has_been_saved)
    val failedRateMessage = stringResource(R.string.failed_to_save_your_rating)

    SeriesDetailsContent(
        state = state,
        seriesDetailsInteractionListener = viewModel,
        rateDialogInteractionListener = viewModel
    )
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SeriesDetailsEffect.NavigateBack -> navController.navigateUp()
                SeriesDetailsEffect.NavigateToCastScreen -> {
                    navController.navigate(
                        Cast(
                            mediaType = MediaType.TV_SHOW.name,
                            mediaId = state.tvShowId
                        )
                    )
                }

                SeriesDetailsEffect.NavigateToLoginScreenEffect -> navController.navigate(
                    Route.Login
                )

                is SeriesDetailsEffect.NavigateToSeriesDetails -> {
                    navController.navigate(SeriesDetails(effect.tvShowId))
                }

                is SeriesDetailsEffect.LaunchSeriesVideoEffect -> openYouTubeVideo(context ,effect.url ){
                    SnackBarManager.showError(context.getString(com.amsterdam.ui.R.string.video_launch_error))
                }

                SeriesDetailsEffect.ShowRatingSuccessSnackBar -> SnackBarManager.showSuccess(message = successRateMessage)
                SeriesDetailsEffect.ShowRatingErrorSnackBar -> SnackBarManager.showError(message = failedRateMessage)
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SeriesDetailsContent(
    state: SeriesDetailsUiState,
    seriesDetailsInteractionListener: SeriesDetailsInteractionListener,
    rateDialogInteractionListener: RateDialogInteractionListener
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
    val scrollOffset = remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset }
    }
    var headerHeight by remember { mutableStateOf(0) }

    val appBarColor by animateColorAsState(
        targetValue = if (scrollOffset.value > 8) AppTheme.color.surface else Color.Transparent,
        animationSpec = tween(800),
        label = "AppBarScrollColor"
    )

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
    val deviceWidth = configuration.screenWidthDp

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage, state.postersUrls.size) {
        if (state.postersUrls.size > 1) {
            coroutineScope.launch {
                snapshotFlow { pagerState.isScrollInProgress }
                    .distinctUntilChanged()
                    .filter { !it }
                    .collect {
                        delay(4000)
                        val nextPage = (pagerState.currentPage + 1) % 10
                        pagerState.animateScrollToPage(nextPage)
                    }
            }
        }
    }

    val topPadding by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex >= 1) {
                96.dp
            } else {
                0.dp
            }
        }
    }
    val animatedTopPadding by animateDpAsState(
        targetValue = topPadding,
        animationSpec = tween(animationDuration), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surface)
            .navigationBarsPadding()
    ) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                CenterOfScreenContainer(
                    unneededSpace = headerHeight.dp,
                ) {
                    NoNetworkContainer(
                        onClickRetry = seriesDetailsInteractionListener::onClickRetryButton,
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier,
            visible = state.isLoginDialogVisible
        ) {
            MustLoginDialog(
                title = state.dialogType.getMovieAndSeriesDetailsDialogTitle(),
                onDismiss = seriesDetailsInteractionListener::onCancelClicked,
                onClickLogin = seriesDetailsInteractionListener::onNavigateToLoginClicked,
            )
        }

        AnimatedVisibility(
            visible = state.rateDialogUiState.isVisible,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            with(state.rateDialogUiState){
                RateDialog(
                    interaction = rateDialogInteractionListener,
                    isSubmittingEnabled = isSubmittingEnabled,
                    isLoading = isLoading,
                    selectedStarIndex = selectedStarIndex,

                    )
            }
        }

        AnimatedVisibility(
            !state.isLoading && !state.networkError,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.color.surface)
                        .padding(top = animatedTopPadding)
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
                                state.rating,
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
                                isActive = state.videoUrl.isNotBlank(),
                                onClick = seriesDetailsInteractionListener::onPlayVideoClicked
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

                                if (state.categories.isNotEmpty()) {
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
                                    description = state.description,
                                    isExpanded = state.isDescriptionExpanded,
                                    onToggleExpansion = seriesDetailsInteractionListener::onDescriptionExpansionToggled
                                )
                                CastSection(
                                    modifier = Modifier.padding(top = 24.dp),
                                    actors = state.cast,
                                    onClickAllCast = seriesDetailsInteractionListener::onClickShowAllCast
                                )
                                Spacer(
                                    modifier = Modifier
                                        .padding(top = 24.dp)
                                        .requiredWidth(screenWidthDp)
                                        .height(1.dp)
                                        .background(AppTheme.color.stroke)
                                )
                                SeriesExtrasSection(
                                    modifier = Modifier
                                        .padding(top = 12.dp)
                                        .onGloballyPositioned { coordinates ->
                                            seriesExtrasSectionYOffsetDp =
                                                coordinates.positionOnScreen().y.dp
                                        },
                                    extras = state.extraItem,
                                    onClickExtras = seriesDetailsInteractionListener::onClickSeriesExtraItem
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
                                    seasonsSection(
                                        seasons = state.seasons,
                                        interaction = seriesDetailsInteractionListener
                                    )
                                }

                                SeriesExtras.MORE_LIKE_THIS -> moreLikeSection(
                                    similarMovies = state.similarSeries,
                                    deviceWidth = deviceWidth,
                                    onClick = { movieId ->
                                        seriesDetailsInteractionListener.onClickSimilarMovie(movieId)
                                    }
                                )

                                SeriesExtras.REVIEWS -> reviewSection(state.reviews, seriesDetailsInteractionListener)
                                SeriesExtras.GALLERY -> gallerySection(
                                    gallery = state.gallery,
                                    deviceWidth = deviceWidth
                                )

                                SeriesExtras.COMPANY_PRODUCTION -> companyProductionSection(
                                    state.productionCompanies, deviceWidth
                                )
                            }
                        }

                    item {
                        val lastVisibleItemInfo by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull() } }
                        val totalItemsCount by remember { derivedStateOf { listState.layoutInfo.totalItemsCount } }
                        val spacerHeight: Dp by remember {
                            derivedStateOf {
                                if (seriesExtrasSectionYOffsetDp > 0.dp || (totalItemsCount > 0 && lastVisibleItemInfo?.index == totalItemsCount - 1)) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(appBarColor)
        ) {
            DefaultAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .statusBarsPadding()
                    .onSizeChanged { headerHeight = it.height },
                firstOption = painterResource(R.drawable.ic_outlined_star),
                lastOption = painterResource(R.drawable.ic_outlined_add_to_favourite),
                onNavigateBackClicked = seriesDetailsInteractionListener::onNavigateBack,
                onFirstOptionClicked = seriesDetailsInteractionListener::onRateClicked,
                onLastOptionClicked = seriesDetailsInteractionListener::onAddToListClicked
            )
            HorizontalDivider(color = dividerColor)
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

    if (!items.all { it.isEmpty() }) {
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
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
@ThemeAndLocalePreviews
private fun SeriesDetailsContentPreview() {
    AflamiTheme {
        SeriesDetailsContent(
            state = SeriesDetailsUiState(),
            seriesDetailsInteractionListener = object : SeriesDetailsInteractionListener {
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
                override fun onDescriptionExpansionToggled() {}
                override fun onReviewExpansionToggled(reviewId: String) {}
                override fun onPlayVideoClicked() {}

                                                                                         },
            rateDialogInteractionListener = object : RateDialogInteractionListener{
                override fun onClickCancel() {}
                override fun onClickSubmit() {}
                override fun onChangeRating(newRate: Int) {}

            }
        )
    }
}