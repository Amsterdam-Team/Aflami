package com.amsterdam.ui.screens.movieDetails

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.ui.components.RatingChip
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.MustLoginDialog
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.movieDetails.components.CastSection
import com.amsterdam.ui.screens.movieDetails.components.CategoryChip
import com.amsterdam.ui.screens.movieDetails.components.CompanyProductionSection
import com.amsterdam.ui.screens.movieDetails.components.DescriptionSection
import com.amsterdam.ui.screens.movieDetails.components.GallerySection
import com.amsterdam.ui.screens.movieDetails.components.MoreLikeSection
import com.amsterdam.ui.screens.movieDetails.components.MovieExtrasSection
import com.amsterdam.ui.screens.movieDetails.components.MovieInfoSection
import com.amsterdam.ui.screens.movieDetails.components.PageIndicator
import com.amsterdam.ui.screens.movieDetails.components.PlayButton
import com.amsterdam.ui.screens.movieDetails.components.ReviewSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreLabel
import com.amsterdam.ui.utils.safeNavigate
import com.amsterdam.viewmodel.cast.MediaType
import com.amsterdam.viewmodel.movieDetails.MovieDetailsEffect
import com.amsterdam.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.movieDetails.MovieDetailsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(viewModel: MovieDetailsViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsState()
    val navController = LocalNavController.current

    MovieContent(
        state = state.value,
        interactionListener = viewModel,
    )
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            effect?.let {
                when (effect) {
                    MovieDetailsEffect.NavigateBackEffect -> navController.popBackStack()
                    MovieDetailsEffect.NavigateToCastsScreenEffect -> {
                        navController.safeNavigate(
                            Route.Cast(
                                mediaType = MediaType.MOVIE.name,
                                mediaId = state.value.movieId
                            )
                        )
                    }
                    MovieDetailsEffect.NavigateToLoginScreenEffect -> navController.safeNavigate(
                        Route.Login
                    )
                }
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MovieContent(
    state: MovieDetailsUiState,
    interactionListener: MovieDetailsInteractionListener,
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp by remember { mutableStateOf(configuration.screenWidthDp.dp) }
    val listState = rememberLazyListState()
    val animationDuration by remember { mutableIntStateOf(1000) }
    val pagerState = rememberPagerState { state.moviePostersUrl.size }

    LaunchedEffect(true) {
        while (true) {
            delay(4000)
            pagerState.animateScrollToPage(((pagerState.currentPage + 1) % 10))
        }
    }
    AnimatedVisibility(
        state.isLoading,
        enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration)),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingContainer()
        }
    }

    AnimatedVisibility(
        state.networkError,
        enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration)),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NoNetworkContainer(
                onClickRetry = interactionListener::onClickRetryRequest,
            )
        }
    }
    AnimatedVisibility(
        modifier = Modifier,
        visible = state.isLoginDialogVisible
    ) {
        MustLoginDialog(
            title = state.dialogType.getMovieAndSeriesDetailsDialogTitle(),
            onDismiss = interactionListener::onCancelClicked,
            onClickLogin = interactionListener::onNavigateToLoginClicked,
        )
    }
    AnimatedVisibility(
        !state.isLoading && !state.networkError,
        enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration)),
    ) {
        LazyColumn(
            state = listState,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(AppTheme.color.surface)
                    .navigationBarsPadding(),
        ) {
            item {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(263.dp),
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        SafeImageView(
                            model = state.moviePostersUrl[page],
                            contentDescription = "",
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .animateContentSize(),
                            onLoading = { ImageLoadingIndicator() },
                            onError = { ImageErrorIndicator() },
                        )

                    }

                    PageIndicator(
                        modifier = Modifier
                            .zIndex(1f)
                            .padding(4.dp)
                            .background(AppTheme.color.primaryVariant, RoundedCornerShape(100.dp))
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                            .align(Alignment.BottomEnd),
                        numberOfPages = state.moviePostersUrl.size,
                        selectedPage = pagerState.currentPage
                    )

                    DefaultAppBar(
                        modifier =
                            Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .statusBarsPadding(),
                        firstOption = painterResource(R.drawable.ic_outlined_star),
                        lastOption = painterResource(R.drawable.ic_outlined_add_to_favourite),
                        onNavigateBackClicked = interactionListener::onClickBack,
                        onFirstOptionClicked = interactionListener::onRateClicked,
                        onLastOptionClicked = interactionListener::onAddToListClicked,
                    )
                    RatingChip(
                        state.rating,
                        modifier =
                            Modifier
                                .align(Alignment.BottomStart)
                                .padding(bottom = 4.dp, start = 4.dp, end = 4.dp),
                    )
                }
            }
            item {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(AppTheme.color.surface),
                ) {
                    PlayButton(
                        modifier =
                            Modifier
                                .align(Alignment.CenterHorizontally)
                                .offset(y = (-32).dp),
                        isActive = state.hasVideo,
                    )
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .offset(y = (-20).dp),
                    ) {
                        Text(
                            text = state.movieTitle,
                            style = AppTheme.textStyle.title.large,
                            color = AppTheme.color.title,
                        )
                        LazyRow(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(state.categories) {
                                CategoryChip(categoryName = getMovieGenreLabel(it))
                            }
                        }
                        MovieInfoSection(
                            modifier = Modifier.padding(top = 8.dp),
                            releaseDate = state.releaseDate,
                            movieLength = state.movieLength,
                            originCountry = state.originCountry,
                        )
                        DescriptionSection(
                            modifier = Modifier.padding(top = 24.dp),
                            description = state.description,
                        )
                        CastSection(
                            modifier = Modifier.padding(top = 24.dp),
                            actors = state.actors.take(10),
                            onClickAllCast = interactionListener::onClickShowAllCast,
                        )
                        Spacer(
                            modifier =
                                Modifier
                                    .padding(top = 24.dp)
                                    .requiredWidth(screenWidthDp)
                                    .height(1.dp)
                                    .background(AppTheme.color.stroke),
                        )
                        MovieExtrasSection(
                            modifier = Modifier.padding(top = 12.dp),
                            extras = state.extraItem,
                            onClickExtras = interactionListener::onClickMovieExtras,
                        )
                    }
                }
            }

            state.extraItem
                .find { it.isSelected }
                ?.item
                ?.let { selectedExtra ->
                    when (selectedExtra) {
                        MovieExtras.MORE_LIKE_THIS -> MoreLikeSection(state.similarMovies)
                        MovieExtras.REVIEWS -> ReviewSection(state.reviews)
                        MovieExtras.GALLERY -> GallerySection(state.gallery)
                        MovieExtras.COMPANY_PRODUCTION -> CompanyProductionSection(state.productionCompany)
                    }
                }
        }
    }
}


@Composable
@ThemeAndLocalePreviews
private fun SearchByActorContentPreview() {
    AflamiTheme {
        MovieContent(
            MovieDetailsUiState(),
            interactionListener =
                object : MovieDetailsInteractionListener {
                    override fun onClickMovieExtras(movieExtras: MovieExtras) {}
                    override fun onClickShowAllCast() {}
                    override fun onClickBack() {}
                    override fun onClickRetryRequest() {}
                    override fun onAddToListClicked() {}
                    override fun onRateClicked() {}
                    override fun onNavigateToLoginClicked() {}
                    override fun onCancelClicked() {}
                },
        )
    }
}
