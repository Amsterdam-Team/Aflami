package com.example.ui.screens.movieDetails

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
import androidx.compose.foundation.pager.VerticalPager
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
import com.example.designsystem.R
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.components.RatingChip
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.application.LocalNavController
import com.example.ui.components.NoNetworkContainer
import com.example.ui.components.appBar.DefaultAppBar
import com.example.ui.navigation.Route
import com.example.ui.screens.movieDetails.components.CastSection
import com.example.ui.screens.movieDetails.components.CategoryChip
import com.example.ui.screens.movieDetails.components.CompanyProductionSection
import com.example.ui.screens.movieDetails.components.DescriptionSection
import com.example.ui.screens.movieDetails.components.GallerySection
import com.example.ui.screens.movieDetails.components.MoreLikeSection
import com.example.ui.screens.movieDetails.components.MovieExtrasSection
import com.example.ui.screens.movieDetails.components.MovieInfoSection
import com.example.ui.screens.movieDetails.components.NoMovieImageHolder
import com.example.ui.screens.movieDetails.components.PageIndicator
import com.example.ui.screens.movieDetails.components.PlayButton
import com.example.ui.screens.movieDetails.components.ReviewSection
import com.example.ui.screens.search.sections.filterDialog.genre.getMovieGenreLabel
import com.example.viewmodel.movieDetails.MovieDetailsEffect
import com.example.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.example.viewmodel.movieDetails.MovieDetailsUiState
import com.example.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.example.viewmodel.movieDetails.MovieDetailsViewModel
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
        viewModel.effect.collectLatest {
            when (it) {
                MovieDetailsEffect.NavigateBackEffect -> navController.popBackStack()
                MovieDetailsEffect.NavigateToCastsScreenEffect ->
                    navController.navigate(
                        Route.Cast(state.value.movieId),
                    )

                null -> {}
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
            pagerState.animateScrollToPage(((pagerState.currentPage + 1) % 10 ))
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
                    VerticalPager(
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
                            onError = { NoMovieImageHolder() },
                        )

                    }

                    PageIndicator(
                        modifier = Modifier
                            .zIndex(1f)
                            .padding(4.dp)
                            .background(AppTheme.color.primaryVariant,RoundedCornerShape(100.dp))
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
                            actors = state.actors,
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
                    override fun onClickMovieExtras(movieExtras: MovieExtras) {
                    }

                    override fun onClickShowAllCast() {
                    }

                    override fun onClickBack() {
                    }

                    override fun onClickRetryRequest() {
                    }
                },
        )
    }
}
