package com.amsterdam.ui.screens.movieDetails

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.AddToListDialog
import com.amsterdam.ui.components.CreateNewListDialog
import com.amsterdam.ui.components.MustLoginDialog
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.RatingChip
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.components.details.DetailsPostersPager
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.navigation.Route.Cast
import com.amsterdam.ui.navigation.Route.MovieDetails
import com.amsterdam.ui.screens.movieDetails.components.CategoryChip
import com.amsterdam.ui.screens.movieDetails.components.DescriptionSection
import com.amsterdam.ui.screens.movieDetails.components.MovieCastSection
import com.amsterdam.ui.screens.movieDetails.components.MovieExtrasSection
import com.amsterdam.ui.screens.movieDetails.components.MovieInfoSection
import com.amsterdam.ui.screens.movieDetails.components.PlayButton
import com.amsterdam.ui.screens.movieDetails.components.RateDialog
import com.amsterdam.ui.screens.movieDetails.components.companyProductionSection
import com.amsterdam.ui.screens.movieDetails.components.gallerySection
import com.amsterdam.ui.screens.movieDetails.components.moreLikeSection
import com.amsterdam.ui.screens.movieDetails.components.reviewMovieSection
import com.amsterdam.ui.screens.openYouTubeVideo
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreLabel
import com.amsterdam.ui.utils.SavedStateKeys.REFRESH_AFTER_RATING
import com.amsterdam.ui.utils.navigateUpWithFlag
import com.amsterdam.viewmodel.movieDetails.MovieDetailsEffect
import com.amsterdam.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.movieDetails.MovieDetailsViewModel
import com.amsterdam.viewmodel.movieDetails.UserListUiState
import com.amsterdam.viewmodel.myRating.RateDialogInteractionListener
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun MovieDetailsScreen(viewModel: MovieDetailsViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    val successRateMessage = stringResource(R.string.your_rating_has_been_saved)
    val failedRateMessage = stringResource(R.string.failed_to_save_your_rating)

    val context = LocalContext.current

    BackHandler { navController.navigateUpWithFlag(flagName = REFRESH_AFTER_RATING, value = true) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                MovieDetailsEffect.NavigateBackEffect -> {
                    navController.navigateUpWithFlag(flagName = REFRESH_AFTER_RATING, value = true)
                }

                MovieDetailsEffect.NavigateToCastsScreenEffect -> {
                    navController.navigate(
                        Cast(
                            mediaType = MediaType.MOVIE.name, mediaId = state.value.movieId
                        )
                    )
                }

                MovieDetailsEffect.NavigateToLoginScreenEffect -> navController.navigate(
                    Route.Login
                )

                is MovieDetailsEffect.NavigateToMovieDetails -> {
                    navController.navigate(
                        MovieDetails(effect.movieId)
                    )
                }

                MovieDetailsEffect.ShowRatingSuccessSnackBar -> SnackBarManager.showSuccess(message = successRateMessage)

                MovieDetailsEffect.ShowRatingErrorSnackBar -> SnackBarManager.showError(message = failedRateMessage)

                is MovieDetailsEffect.LaunchMovieVideoEffect -> openYouTubeVideo(
                    context, effect.url
                ) {
                    SnackBarManager.showError(context.getString(com.amsterdam.ui.R.string.video_launch_error))
                }

                MovieDetailsEffect.MovieAddedToListError -> {
                    SnackBarManager.showError(
                        context.getString(com.amsterdam.ui.R.string.failed_to_add_to_list),
                    )
                }

                MovieDetailsEffect.MovieAddedToListSuccessfully -> {
                    SnackBarManager.showSuccess(
                        context.getString(com.amsterdam.ui.R.string.added_to_list_successfully),
                    )
                }

                MovieDetailsEffect.FailedToCreateList -> {
                    SnackBarManager.showError(
                        message = context.resources.getString(R.string.general_error_message),
                    )
                }

                MovieDetailsEffect.ListCreatedSuccessfully -> {
                    SnackBarManager.showSuccess(
                        message = context.resources.getString(R.string.list_added_success_message),
                    )
                }
            }
        }
    }

    MovieContent(
        state = state.value,
        movieDetailsInteractionListener = viewModel,
        rateDialogInteractionListener = viewModel
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MovieContent(
    state: MovieDetailsUiState,
    movieDetailsInteractionListener: MovieDetailsInteractionListener,
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

    val scrollOffset by remember {
        derivedStateOf { parentLazyListState.firstVisibleItemScrollOffset }
    }

    LaunchedEffect(parentLazyListState.isScrollInProgress) {
        canChildScroll = !parentLazyListState.canScrollForward
    }

    LaunchedEffect(childLazyListState.isScrollInProgress) {
        canChildScroll = childLazyListState.canScrollBackward
    }

    val screenWidthDp by remember { mutableStateOf(configuration.screenWidthDp.dp) }
    val listState = rememberLazyListState()
    val animationDuration by remember { mutableIntStateOf(1000) }
    val pagerState = rememberPagerState { state.moviePostersUrl.size }
    val deviceWidth = configuration.screenWidthDp

    val surface = AppTheme.color.surface
    val transparent = AppTheme.color.surface.copy(alpha = 0f)
    val stroke = AppTheme.color.stroke

    val appBarColor by animateColorAsState(
        targetValue = if (scrollOffset > 8) AppTheme.color.surface else Color.Transparent,
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

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.moviePostersUrl.size) {
        if (state.moviePostersUrl.size > 1) {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surface)
            .navigationBarsPadding()
    ) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                CenterOfScreenContainer(unneededSpace = appBarHeight) {
                    NoNetworkContainer(
                        onClickRetry = movieDetailsInteractionListener::onClickRetryRequest,
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier,
            visible = state.isCreateNewListDialogVisible,
        ) {
            CreateNewListDialog(
                isCreateListLoading = state.isCreateListLoading,
                listName = state.listName,
                onListNameChange = movieDetailsInteractionListener::onChangeListName,
                onCreateListClick = movieDetailsInteractionListener::onClickCreateNewList,
                onDismiss = movieDetailsInteractionListener::onClickCancel,
            )
        }

        AnimatedVisibility(
            modifier = Modifier, visible = state.isLoginDialogVisible
        ) {
            MustLoginDialog(
                title = state.dialogType.getMovieAndSeriesDetailsDialogTitle(),
                onDismiss = movieDetailsInteractionListener::onClickCancel,
                onClickLogin = movieDetailsInteractionListener::onClickNavigateToLogin,
            )
        }

        AnimatedVisibility(
            modifier = Modifier,
            visible = state.isAddToListDialogVisible,
        ) {
            AddToListDialog(
                userLists = state.userLists,
                selectedList = state.selectedList,
                isAddMovieToListLoading = state.isAddMovieToListLoading,
                onSelectedListChange = movieDetailsInteractionListener::onSelectedListChange,
                onAddToSelectedList = { listId ->
                    movieDetailsInteractionListener.onSaveMovieToList(
                        movieId = state.movieId,
                        listId = listId,
                    )
                },
                onCreateNewList = movieDetailsInteractionListener::onClickCreateList,
                onDismiss = movieDetailsInteractionListener::onClickCancel,
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
                    selectedStarIndex = selectedStarIndex
                )
            }
        }
        AnimatedVisibility(
            !state.isLoading && !state.networkError,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration)),
        ) {
            LazyColumn(
                state = parentLazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.color.surface)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(263.dp),
                    ) {
                        if (state.moviePostersUrl.isEmpty()) {
                            ImageErrorIndicator()
                        } else {
                            DetailsPostersPager(
                                pagerState = pagerState, postersUrl = state.moviePostersUrl
                            )
                        }

                        RatingChip(
                            state.rating,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(bottom = 4.dp, start = 4.dp, end = 4.dp),
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.color.surface),
                    ) {
                        PlayButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .offset(y = (-32).dp),
                            isActive = state.videoUrl.isNotBlank(),
                            onClick = movieDetailsInteractionListener::onClickPlayVideo,
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-20).dp),
                        ) {
                            Text(
                                text = state.movieTitle,
                                style = AppTheme.textStyle.title.large,
                                color = AppTheme.color.title,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                items(state.categories) {
                                    CategoryChip(categoryName = getMovieGenreLabel(it))
                                }
                            }
                            MovieInfoSection(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .padding(horizontal = 16.dp),
                                releaseDate = state.releaseDate,
                                movieLength = state.movieLength,
                                originCountry = state.originCountry,
                            )
                            DescriptionSection(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .padding(horizontal = 16.dp),
                                description = state.description,
                                isExpanded = state.isDescriptionExpanded,
                                onToggleExpansion = movieDetailsInteractionListener::onDescriptionExpansionToggled
                            )
                            MovieCastSection(
                                modifier = Modifier.padding(top = 24.dp),
                                actors = state.actors.take(10),
                                onClickAllCast = movieDetailsInteractionListener::onClickShowAllCast,
                            )
                            Spacer(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .requiredWidth(screenWidthDp)
                                    .height(1.dp)
                                    .background(AppTheme.color.stroke),
                            )
                            MovieExtrasSection(
                                modifier = Modifier.padding(top = 12.dp),
                                extras = state.extraItem,
                                onClickExtras = movieDetailsInteractionListener::onClickMovieExtras,
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
                                MovieExtras.MORE_LIKE_THIS -> moreLikeSection(
                                    similarMovies = state.similarMovies,
                                    deviceWidth = deviceWidth,
                                    onClick = { selectedMovieId ->
                                        movieDetailsInteractionListener.onClickSimilarMovie(
                                            selectedMovieId
                                        )
                                    })

                                MovieExtras.REVIEWS -> reviewMovieSection(
                                    state.reviews, movieDetailsInteractionListener
                                )

                                MovieExtras.GALLERY -> gallerySection(
                                    gallery = state.gallery, deviceWidth = deviceWidth
                                )

                                MovieExtras.COMPANY_PRODUCTION -> companyProductionSection(
                                    state.productionCompany, deviceWidth = deviceWidth
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
                .onSizeChanged { appBarHeight = with(density) { it.height.toDp() } },
        ) {
            DefaultAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .statusBarsPadding()
                    .zIndex(10f),
                firstOption = painterResource(R.drawable.ic_outlined_star),
                lastOption = painterResource(R.drawable.ic_outlined_add_to_favourite),
                onNavigateBackClicked = movieDetailsInteractionListener::onClickBack,
                onFirstOptionClicked = movieDetailsInteractionListener::onClickRate,
                onLastOptionClicked = movieDetailsInteractionListener::onClickAddToList,
            )

            HorizontalDivider(color = dividerColor)
        }
    }

}

@Composable
@ThemeAndLocalePreviews
private fun SearchByActorContentPreview() {
    AflamiTheme {
        MovieContent(
            MovieDetailsUiState(),
            movieDetailsInteractionListener = object : MovieDetailsInteractionListener {
                override fun onClickMovieExtras(movieExtras: MovieExtras) {}
                override fun onClickShowAllCast() {}
                override fun onClickBack() {}
                override fun onClickRetryRequest() {}

                override fun onClickAddToList() {}

                override fun onSaveMovieToList(
                    movieId: Long, listId: Long
                ) {
                }

                override fun onClickCreateList() {}

                override fun onChangeListName(listName: String) {}

                override fun onClickCreateNewList() {}

                override fun onSelectedListChange(selectedList: UserListUiState) {}

                override fun onClickRate() {}

                override fun onClickNavigateToLogin() {}

                override fun onClickCancel() {}
                override fun onClickSimilarMovie(movieId: Long) {}
                override fun onDescriptionExpansionToggled() {}
                override fun onReviewExpansionToggled(reviewId: String) {}

                override fun onClickPlayVideo() {}
            },
            rateDialogInteractionListener = object : RateDialogInteractionListener {
                override fun onClickCancelRateDialog() {}
                override fun onClickSubmit() {}
                override fun onChangeRating(newRate: Int) {}

            })
    }
}
