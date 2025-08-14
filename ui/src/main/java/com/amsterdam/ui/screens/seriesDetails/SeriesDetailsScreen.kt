package com.amsterdam.ui.screens.seriesDetails

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.EpisodeCard
import com.amsterdam.ui.components.MustLoginDialog
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.RatingChip
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.components.details.DetailsPostersPager
import com.amsterdam.ui.screens.movieDetails.components.CategoryChip
import com.amsterdam.ui.screens.movieDetails.components.DescriptionSection
import com.amsterdam.ui.screens.movieDetails.components.EmptyStateText
import com.amsterdam.ui.screens.movieDetails.components.PlayButton
import com.amsterdam.ui.screens.movieDetails.components.RateDialog
import com.amsterdam.ui.screens.movieDetails.components.gallerySection
import com.amsterdam.ui.screens.movieDetails.getMovieAndSeriesDetailsDialogTitle
import com.amsterdam.ui.screens.movieDetails.getSeriesExtrasSectionItemInfo
import com.amsterdam.ui.screens.openYouTubeVideo
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getTvShowGenreLabel
import com.amsterdam.ui.screens.seriesDetails.component.TvShowCastSection
import com.amsterdam.ui.screens.seriesDetails.component.companyProductionTvShowSection
import com.amsterdam.ui.screens.seriesDetails.component.moreTvShowLikeSection
import com.amsterdam.ui.screens.seriesDetails.component.reviewTvShowSection
import com.amsterdam.ui.utils.SavedStateKeys.REFRESH_AFTER_RATING
import com.amsterdam.viewmodel.myRating.RateDialogInteractionListener
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsEffect
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsInteractionListener
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.EpisodeUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsViewModel
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.uiStates.MediaType
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
    val navigationManager = LocalNavManager.current
    val context = LocalContext.current
    val successRateMessage = stringResource(R.string.your_rating_has_been_saved)
    val failedRateMessage = stringResource(R.string.failed_to_save_your_rating)

    BackHandler { navigationManager.navigateUpWithFlag(flagName = REFRESH_AFTER_RATING, value = true) }

    SeriesDetailsContent(
        state = state,
        seriesDetailsInteractionListener = viewModel,
        rateDialogInteractionListener = viewModel
    )
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SeriesDetailsEffect.NavigateBack -> {
                    navigationManager.navigateUpWithFlag(flagName = REFRESH_AFTER_RATING, value = true)
                }

                SeriesDetailsEffect.NavigateToCastScreen -> {
                    navigationManager.toCast(mediaType = MediaType.TV_SHOW.name, mediaId = state.tvShowId)
                }

                SeriesDetailsEffect.NavigateToLoginScreenEffect -> navigationManager.toLogin()

                is SeriesDetailsEffect.NavigateToSeriesDetails -> {
                    navigationManager.toSeriesDetails(effect.tvShowId)
                }

                is SeriesDetailsEffect.ShowEpisodeTrailerNotFound -> {
                    SnackBarManager.showError(context.getString(com.amsterdam.ui.R.string.video_launch_error))
                }

                is SeriesDetailsEffect.LaunchSeriesVideoEffect -> openYouTubeVideo(
                    context, effect.url
                ) {
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
    val density = LocalDensity.current

    val parentLazyListState = rememberLazyListState()
    val childLazyListState = rememberLazyListState()
    var canChildScroll by remember { mutableStateOf(!parentLazyListState.canScrollForward) }
    var appBarHeight by remember { mutableStateOf(0.dp) }
    val navigationBarPadding = with(density) { WindowInsets.safeDrawing.getBottom(this).toDp() }
    val contentHeightDp = configuration.screenHeightDp.dp

    LaunchedEffect(parentLazyListState.isScrollInProgress) {
        canChildScroll = !parentLazyListState.canScrollForward
    }

    LaunchedEffect(childLazyListState.isScrollInProgress) {
        canChildScroll = childLazyListState.canScrollBackward
    }

    val screenWidthDp by remember { mutableStateOf(configuration.screenWidthDp.dp) }
    val animationDuration by remember { mutableIntStateOf(1000) }
    val surface = AppTheme.color.surface
    val transparent = AppTheme.color.surface.copy(alpha = 0f)
    val stroke = AppTheme.color.stroke
    val scrollOffset by remember {
        derivedStateOf { parentLazyListState.firstVisibleItemScrollOffset }
    }

    val appBarColor by animateColorAsState(
        targetValue = if (scrollOffset > 8) AppTheme.color.surface else Color.Transparent,
        animationSpec = tween(800),
        label = "AppBarScrollColor"
    )

    val dividerColor by remember {
        derivedStateOf {
            if (parentLazyListState.firstVisibleItemIndex != 0) {
                stroke
            } else {
                transparent
            }
        }
    }
    val pagerState = rememberPagerState { state.postersUrls.size }
    val deviceWidth = configuration.screenWidthDp

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.postersUrls.size) {
        if (state.postersUrls.size > 1) {
            coroutineScope.launch {
                snapshotFlow { pagerState.isScrollInProgress }.distinctUntilChanged().filter { !it }
                    .collectLatest {
                        delay(4000)
                        val nextPage = (pagerState.currentPage + 1) % 10
                        pagerState.animateScrollToPage(nextPage)
                    }
            }
        }
    }

    val topPadding by remember {
        derivedStateOf {
            if (parentLazyListState.firstVisibleItemIndex >= 1) {
                96.dp
            } else {
                0.dp
            }
        }
    }
    val animatedTopPadding by animateDpAsState(
        targetValue = topPadding, animationSpec = tween(animationDuration), label = ""
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
            state.networkError,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                CenterOfScreenContainer(
                    unneededSpace = appBarHeight,
                ) {
                    NoNetworkContainer(
                        onClickRetry = seriesDetailsInteractionListener::onClickRetryButton,
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier, visible = state.isLoginDialogVisible
        ) {
            MustLoginDialog(
                title = state.dialogType.getMovieAndSeriesDetailsDialogTitle(),
                onDismiss = seriesDetailsInteractionListener::onCancelClicked,
                onClickLogin = seriesDetailsInteractionListener::onNavigateToLoginClicked,
            )
        }

        AnimatedVisibility(
            visible = state.rateDialogUiState.isVisible, enter = expandIn(), exit = shrinkOut()
        ) {
            with(state.rateDialogUiState) {
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
            LazyColumn(
                state = parentLazyListState,
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
                                pagerState = pagerState, postersUrl = state.postersUrls
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
                            TvShowCastSection(
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
                                    .padding(top = 12.dp),
                                extras = state.extraItem,
                                onClickExtras = seriesDetailsInteractionListener::onClickSeriesExtraItem
                            )
                        }

                    }
                }
                item {
                    LazyColumn(
                        state = childLazyListState,
                        userScrollEnabled = canChildScroll,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = contentHeightDp - appBarHeight - navigationBarPadding)
                            .animateContentSize(tween(500))
                    ) {
                        state.extraItem.find { it.isSelected }?.item?.let { selectedExtra ->
                            when (selectedExtra) {
                                SeriesExtras.SEASONS -> {
                                    seasonsSection(
                                        seasons = state.seasons,
                                        interaction = seriesDetailsInteractionListener
                                    )
                                }

                                SeriesExtras.MORE_LIKE_THIS -> moreTvShowLikeSection(
                                    similarMovies = state.similarSeries,
                                    deviceWidth = deviceWidth,
                                    onClick = { movieId ->
                                        seriesDetailsInteractionListener.onClickSimilarMovie(
                                            movieId
                                        )
                                    })

                                SeriesExtras.REVIEWS -> reviewTvShowSection(
                                    state.reviews, seriesDetailsInteractionListener
                                )

                                SeriesExtras.GALLERY -> gallerySection(
                                    gallery = state.gallery, deviceWidth = deviceWidth
                                )

                                SeriesExtras.COMPANY_PRODUCTION -> companyProductionTvShowSection(
                                    state.productionCompanies, deviceWidth
                                )
                            }
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(appBarColor)
                .onSizeChanged { appBarHeight = with(density) { it.height.toDp() } }) {
            DefaultAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .statusBarsPadding(),
                firstOption = painterResource(R.drawable.ic_outlined_star),
                onNavigateBackClicked = seriesDetailsInteractionListener::onNavigateBack,
                onFirstOptionClicked = seriesDetailsInteractionListener::onClickRate,
            )
            HorizontalDivider(color = dividerColor)
        }


    }
}

@Composable
private fun SeriesInfoSection(
    modifier: Modifier = Modifier, airDate: String, seasonCount: String, originCountry: String
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
                onClick = { onClickExtras(it.item) })
        }
    }
}

private fun LazyListScope.seasonsSection(
    seasons: List<SeasonUiState>, interaction: SeriesDetailsInteractionListener
) {
    if (seasons.isEmpty()) {
        item { EmptyStateText(stringResource(R.string.there_is_no_seasons)) }
    } else {
        seasons.forEachIndexed { index, season ->
            stickyHeader {
                SeasonHeader(
                    season = season, onClickSeasonMenu = { seasonNumber ->
                        interaction.onClickSeasonMenu(seasonNumber)
                    })
            }
            val episodes = if (season.isExpanded) season.episodes else emptyList()
            items(episodes, key = { "${it.id}-${season.episodes.indexOf(it)}-${index}" }) {
                EpisodesMenu(season.seasonNumber, it, interaction::onPlayEpisodeClicked)
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
            verticalAlignment = Alignment.CenterVertically) {
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
                modifier = Modifier.size(20.dp),
                painter = if (season.isExpanded) painterResource(R.drawable.ic_arrow_up) else painterResource(
                    R.drawable.ic_arrow_down
                ),
                contentDescription = null,
                tint = AppTheme.color.title,
            )
        }
        HorizontalDivider(color = AppTheme.color.stroke)
    }
}

@Composable
private fun EpisodesMenu(
    seasonNumber: Int,
    episode: EpisodeUiState,
    onPlayEpisodeClicked: (seasonNumber: Int, episodeNumber: Int) -> Unit
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
        onPlayEpisodeClick = {
            onPlayEpisodeClicked(seasonNumber, episode.number)
        },
        isActive = true
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
                override fun onClickRate() {}
                override fun onClickSeasonMenu(seasonNumber: Int) {}
                override fun onNavigateToLoginClicked() {}
                override fun onCancelClicked() {}
                override fun onClickSimilarMovie(movieId: Long) {}
                override fun onDescriptionExpansionToggled() {}
                override fun onReviewExpansionToggled(reviewId: String) {}
                override fun onPlayVideoClicked() {}
                override fun onPlayEpisodeClicked(
                    seasonNumber: Int, episodeNumber: Int
                ) {
                }
            },
            rateDialogInteractionListener = object : RateDialogInteractionListener {
                override fun onClickCancelRateDialog() {}

                override fun onClickSubmit() {}
                override fun onChangeRating(newRate: Int) {}

            })
    }
}