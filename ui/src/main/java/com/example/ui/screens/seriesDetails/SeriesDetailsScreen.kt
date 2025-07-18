package com.example.ui.screens.seriesDetails

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.Chip
import com.example.designsystem.components.EpisodeCard
import com.example.designsystem.components.Icon
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.components.NoNetworkContainer
import com.example.designsystem.components.RatingChip
import com.example.designsystem.components.Text
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.application.LocalNavController
import com.example.ui.navigation.Route
import com.example.ui.screens.movieDetails.components.CastSection
import com.example.ui.screens.movieDetails.components.CategoryChip
import com.example.ui.screens.movieDetails.components.CompanyProductionSection
import com.example.ui.screens.movieDetails.components.DescriptionSection
import com.example.ui.screens.movieDetails.components.GallerySection
import com.example.ui.screens.movieDetails.components.MoreLikeSection
import com.example.ui.screens.movieDetails.components.NoMovieImageHolder
import com.example.ui.screens.movieDetails.components.PlayButton
import com.example.ui.screens.movieDetails.components.ReviewSection
import com.example.ui.screens.movieDetails.getSeriesExtrasSectionItemInfo
import com.example.ui.screens.search.sections.filterDialog.genre.getTvShowGenreLabel
import com.example.viewmodel.seriesDetails.SeriesDetailsEffect
import com.example.viewmodel.seriesDetails.SeriesDetailsInteractionListener
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.EpisodeUiState
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.example.viewmodel.seriesDetails.SeriesDetailsViewModel
import com.example.viewmodel.shared.Selectable
import org.koin.androidx.compose.koinViewModel

@Composable
fun SeriesDetailsScreen(
    viewModel: SeriesDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current
    SeriesDetailsContent(
        state = state,
        interactionListener = viewModel
    )
    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                SeriesDetailsEffect.NavigateBack -> navController.popBackStack()
                SeriesDetailsEffect.NavigateToCastScreen -> navController.navigate(Route.SeriesDetails)
                null -> {}
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SeriesDetailsContent(
    state: SeriesDetailsUiState,
    interactionListener: SeriesDetailsInteractionListener
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp by remember { mutableStateOf(configuration.screenWidthDp.dp) }
    val listState = rememberLazyListState()
    val animationDuration by remember { mutableIntStateOf(1000) }
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
                onClickRetry = interactionListener::onClickRetryButton,
            )
        }
    }

    AnimatedVisibility(
        !state.isLoading && !state.networkError,
        enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration))
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.surface)
                .navigationBarsPadding()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(263.dp)
                ) {
                    SafeImageView(
                        model = state.posterUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .animateContentSize(),
                        onError = { NoMovieImageHolder() }
                    )
                    DefaultAppBar(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .statusBarsPadding(),
                        firstOption = painterResource(R.drawable.ic_outlined_star),
                        lastOption = painterResource(R.drawable.ic_outlined_add_to_favourite),
                        onNavigateBackClicked = interactionListener::onNavigateBack
                    )
                    RatingChip(
                        state.rating,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 4.dp, start = 4.dp, end = 4.dp)
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
                            .padding(horizontal = 16.dp)
                            .offset(y = (-20).dp)
                    ) {
                        Text(
                            text = state.title,
                            style = AppTheme.textStyle.title.large,
                            color = AppTheme.color.title
                        )
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.categories) {
                                CategoryChip(categoryName = getTvShowGenreLabel(it))
                            }
                        }
                        SeriesInfoSection(
                            modifier = Modifier.padding(top = 8.dp),
                            airDate = state.airDate,
                            seasonCount = state.seasonCount,
                            originCountry = state.originCountry
                        )
                        DescriptionSection(
                            modifier = Modifier.padding(top = 24.dp),
                            description = state.description
                        )
                        CastSection(
                            modifier = Modifier.padding(top = 24.dp),
                            actors = state.cast,
                            onClickAllCast = interactionListener::onClickShowAllCast
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .requiredWidth(screenWidthDp)
                                .height(1.dp)
                                .background(AppTheme.color.stroke)
                        )
                        SeriesExtrasSection(
                            modifier = Modifier.padding(top = 12.dp),
                            extras = state.extraItem,
                            onClickExtras = interactionListener::onClickSeriesExtraItem
                        )
                    }

                }
            }

            state.extraItem
                .find { it.isSelected }
                ?.item
                ?.let { selectedExtra ->
                    when (selectedExtra) {
                        SeriesExtras.SEASONS -> SeasonsSection(state.seasons)
                        SeriesExtras.MORE_LIKE_THIS -> MoreLikeSection(state.similarSeries)
                        SeriesExtras.REVIEWS -> ReviewSection(state.reviews)
                        SeriesExtras.GALLERY -> GallerySection(state.gallery)
                        SeriesExtras.COMPANY_PRODUCTION -> CompanyProductionSection(
                            state.productionCompanies
                        )
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

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEachIndexed { index, item ->
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

@Composable
private fun SeriesExtrasSection(
    modifier: Modifier = Modifier,
    extras: List<Selectable<SeriesExtras>>,
    onClickExtras: (SeriesExtras) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        extras.forEach {
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

private fun LazyListScope.SeasonsSection(seasons: List<SeasonUiState>) {
    seasons.forEachIndexed { index, season ->
        stickyHeader {
            SeasonHeader(season = season)
        }

        item {
            EpisodesMenu(episodes = season.episodes)
        }
    }
}

@Composable
private fun SeasonHeader(
    season: SeasonUiState
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = season.title,
            color = AppTheme.color.title,
            style = AppTheme.textStyle.label.small,
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
            painter = if (expanded) painterResource(R.drawable.ic_arrow_up) else
                painterResource(R.drawable.ic_arrow_down),
            contentDescription = null,
            tint = AppTheme.color.title,
        )
    }
}

@Composable
private fun EpisodesMenu(
    episodes: List<EpisodeUiState>
) {
    LazyColumn {
        itemsIndexed(episodes) { index, episode ->
            EpisodeCard(
                episodeBanner = episode.imageUrl,
                episodeRate = episode.rating,
                episodeNumber = episode.number,
                episodeTitle = episode.title,
                episodeTime = episode.duration,
                publishedAt = episode.airDate,
                episodeDescription = episode.description,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                onPlayEpisodeClick = { }
            )
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun SeriesDetailsContentPreview() {
    AflamiTheme {
        SeriesDetailsContent(
            state = SeriesDetailsUiState(),

            interactionListener = object : SeriesDetailsInteractionListener {
                override fun onClickSeriesExtraItem(seriesExtras: SeriesExtras) {}
                override fun onNavigateBack() {}
                override fun onClickRetryButton() {}
                override fun onClickShowAllCast() {}
                override fun onClickSeasonMenu(seasonNumber: Int) {}
            }
        )
    }
}