package com.example.ui.screens.movieDetails

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.viewmodel.movieDetails.MovieDetailsUiState
import com.example.viewmodel.movieDetails.MovieDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.designsystem.R
import com.example.designsystem.components.Chip
import com.example.designsystem.components.ExpandableText
import com.example.designsystem.components.Icon
import com.example.designsystem.components.RatingChip
import com.example.designsystem.components.Text
import com.example.designsystem.components.UpcomingCard
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.designsystem.utils.dropShadow
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.application.LocalNavController
import com.example.ui.navigation.Route
import com.example.ui.screens.searchByCountry.Loading
import com.example.ui.screens.searchByCountry.NoInternetConnection
import com.example.viewmodel.common.Selectable
import com.example.viewmodel.movieDetails.ActorUiState
import com.example.viewmodel.movieDetails.MovieDetailsEffect
import com.example.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.example.viewmodel.movieDetails.MovieExtras
import com.example.viewmodel.movieDetails.ProductionCompanyUiState
import com.example.viewmodel.movieDetails.ReviewUiState
import com.example.viewmodel.movieDetails.SimilarMovieUiState
import kotlinx.coroutines.flow.collectLatest
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
fun MovieDetailsScreen(viewModel: MovieDetailsViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsState()
    val navController = LocalNavController.current

    MovieContent(
        state = state.value,
        interactionListener = viewModel
    )
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                MovieDetailsEffect.NavigateBackEffect -> navController.popBackStack()
                MovieDetailsEffect.NavigateToCastsScreenEffect -> navController.navigate(Route.Cast(state.value.movieId))
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
    AnimatedVisibility(state.isLoading,
        enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration))
    ) {
        Loading()
    }

    AnimatedVisibility(state.networkError,  enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration))) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NoInternetConnection(
                onRetryQuestClicked = interactionListener::onRetryQuestClicked,
            )
        }
    }


    AnimatedVisibility(state.isUnknownError,  enter = fadeIn(tween(animationDuration)),
        exit = fadeOut(tween(animationDuration))) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NoInternetConnection(
                onRetryQuestClicked = interactionListener::onRetryQuestClicked,
            )
        }
    }

    AnimatedVisibility(!state.isLoading && !state.networkError && !state.isUnknownError,
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
                        onError ={ NoMovieImageHolder()}
                    )
                    DefaultAppBar(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .statusBarsPadding(),
                        firstOption = painterResource(R.drawable.ic_outlined_star),
                        lastOption = painterResource(R.drawable.ic_outlined_add_to_favourite),
                        onNavigateBackClicked = interactionListener::onBackClicked
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
                            text = state.movieTitle,
                            style = AppTheme.textStyle.title.large,
                            color = AppTheme.color.title
                        )
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.categoriesNames) {
                                CategoryChip(categoryName = it)
                            }
                        }
                        MovieInfoSection(
                            modifier = Modifier.padding(top = 8.dp),
                            releaseDate = state.releaseDate,
                            movieLength = state.movieLength,
                            originCountry = state.originCountry
                        )
                        DescriptionSection(
                            modifier = Modifier.padding(top = 24.dp),
                            description = state.description
                        )
                        CastSection(
                            modifier = Modifier.padding(top = 24.dp),
                            actors = state.actors,
                            onClickAllCast = interactionListener::onShowAllCastClicked
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .requiredWidth(screenWidthDp)
                                .height(1.dp)
                                .background(AppTheme.color.stroke)
                        )
                        MovieExtrasSection(
                            modifier = Modifier.padding(top = 12.dp),
                            extras = state.extraItem,
                            onClickExtras = interactionListener::onMovieExtrasClicked
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

fun LazyListScope.CompanyProductionSection(companies: List<ProductionCompanyUiState>) {
    itemsIndexed(companies.chunked(2), key = { index, _ -> index }) { index, rowCompanies ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp, end =
                        16.dp, bottom = 8.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            rowCompanies.forEachIndexed { index, company ->
                val maxWidth = if (index == 0) .5f else 1f
                CompanyCard(
                    productionCompany = company,
                    modifier = Modifier.fillMaxWidth(maxWidth)
                )
            }
        }
    }
}

@Composable
fun CompanyCard(productionCompany: ProductionCompanyUiState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(145.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = AppTheme.color.stroke, shape = RoundedCornerShape(16.dp))

    ) {
        SafeImageView(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight(),
            contentDescription = productionCompany.name,
            model = productionCompany.image,
            contentScale = ContentScale.FillBounds,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .zIndex(2f)
                .padding(
                    start = 8.dp, end = 8.dp, bottom =
                        8.dp
                )
        ) {


            Text(
                modifier = Modifier.fillMaxWidth(), text =
                    productionCompany.name,
                maxLines = 1,
                style = AppTheme.textStyle.label.large,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.color.onPrimary
            )
            Text(
                text = productionCompany.country,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.onPrimaryBody
            )
        }
        val overlayDarkColor = AppTheme.color.overlayDark
        Box(
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.BottomCenter)
                .height(82.dp)
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = overlayDarkColor
                        )
                    )
                }
        )
    }
}

fun LazyListScope.GallerySection(gallery: List<String>) {
    itemsIndexed(gallery.chunked(2), key = { index, _ -> index }) { index, rowGallery ->

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp, end =
                        16.dp, bottom = 8.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            rowGallery.forEachIndexed { index, image ->
                val maxWidth = if (index == 0) .5f else 1f

                SafeImageView(
                    modifier = Modifier
                        .fillMaxWidth(maxWidth)
                        .height(145.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription = null,
                    model = image,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}


fun LazyListScope.ReviewSection(reviews: List<ReviewUiState>) {
    if (reviews.isEmpty())
        item {
            EmptyStateText(stringResource(com.example.ui.R.string.there_is_no_reviews))
        }
    else
        itemsIndexed(reviews) { index, item ->
            val topPadding = if (index == 0) 0 else 12
            ReviewCard(item, modifier = Modifier.padding(top = topPadding.dp))
        }
}

@Composable
fun ReviewCard(review: ReviewUiState, modifier: Modifier = Modifier) {
    val strokeColor = AppTheme.color.stroke
    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth,
                    cap = Stroke.DefaultCap
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SafeImageView(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentDescription = review.author,
                model = review.imageUrl
                    ?: "android.resource://your.package.name/${R.drawable.ic_outlined_star}",
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    modifier = Modifier.fillMaxWidth(), text =
                        review.author,
                    maxLines = 1,
                    style = AppTheme.textStyle.title.medium,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.color.title
                )
                Text(
                    text = review.username,
                    style = AppTheme.textStyle.label.small,
                    color = AppTheme.color.hint
                )

            }
            Spacer(Modifier.weight(1f))
            RatingChip(
                review.rating,
                modifier = Modifier
                    .size(40.dp)
                    .padding(bottom = 4.dp)
            )
        }
        ExpandableText(text = review.content)
        Text(
            text = review.date,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.hint
        )
    }
}

fun LazyListScope.MoreLikeSection(similarMovies: List<SimilarMovieUiState>) {
    if (similarMovies.isEmpty())
        item {
            EmptyStateText(stringResource(com.example.ui.R.string.there_is_no_production_company))
        }
    else
        itemsIndexed(similarMovies, key = { index, _ -> index }) { index, similarMovie ->
            val yOffset = if (index == 0) -16 else 0
            UpcomingCard(
                movieImage = similarMovie.posterUrl,
                movieTitle = similarMovie.name,
                movieType = stringResource(R.string.movie),
                movieYear = similarMovie.productionYear,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    .offset(y = yOffset.dp),
                movieRating = similarMovie.rate,
                movieContentDescription = similarMovie.name,
                onClick = {}
            )
        }
}

@Composable
fun EmptyStateText(text: String) {
    Text(
        text = text, modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = AppTheme.textStyle.label.large,
        color = AppTheme.color.body
    )
}


@Composable
fun PlayButton(modifier: Modifier = Modifier,isActive : Boolean) {
    val playButtonColor = if(isActive) AppTheme.color.primary else AppTheme.color.disable
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(
                color = AppTheme.color.surfaceHigh,
                shape = CircleShape
            )
            .padding(2.dp)
            .border(2.dp, AppTheme.color.stroke, shape = CircleShape)
            .dropShadow(CircleShape, Color(0x1F8951FF), spread = 12.dp)
            .background(
                color = AppTheme.color.surfaceHigh,
                shape = CircleShape
            )

    ) {
        Icon(
            painterResource(R.drawable.ic_play),
            contentDescription = null,
            tint = playButtonColor,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
        )
    }
}

@Composable
fun CategoryChip(modifier: Modifier = Modifier, categoryName: String) {
    Row(
        modifier = modifier
            .background(
                color = AppTheme.color.surfaceHigh,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            categoryName,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.primary
        )
    }
}

@Composable
fun MovieInfoSection(
    releaseDate: String,
    movieLength: String,
    originCountry: String,
    modifier: Modifier = Modifier
) {
    val items = listOf(releaseDate, movieLength, originCountry)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
fun DescriptionSection(modifier: Modifier = Modifier, description: String) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.description),
            style = AppTheme.textStyle.headline.small,
            color = AppTheme.color.title,
        )
        ExpandableText(text = description)
    }
}


@Composable
fun CastSection(
    modifier: Modifier = Modifier,
    actors: List<ActorUiState>,
    onClickAllCast: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.cast),
                style = AppTheme.textStyle.headline.small,
                color = AppTheme.color.title,
            )
            Text(
                text = stringResource(R.string.all),
                style = AppTheme.textStyle.label.medium,
                color = AppTheme.color.primary,
                modifier = Modifier.clickable(onClick = onClickAllCast)
            )
        }
        LazyRow(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(actors) {
                ActorCard(actor = it)
            }
        }
    }
}

@Composable
fun ActorCard(modifier: Modifier = Modifier, actor: ActorUiState) {
    Column(modifier = modifier.width(78.dp)) {
        SafeImageView(
            modifier = Modifier
                .size(78.dp)
                .clip(RoundedCornerShape(16.dp)),
            model = actor.photo,
            contentDescription = actor.name
        )
        Text(
            text = actor.name,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.body,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun MovieExtrasSection(
    modifier: Modifier = Modifier,
    extras: List<Selectable<MovieExtras>>,
    onClickExtras: (MovieExtras) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        extras.forEach {
            val extrasSectionItemInfo = it.item.getExtrasSectionItemInfo()
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

@Composable
fun NoMovieImageHolder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(painterResource(R.drawable.ic_film_roll),
            contentDescription = null)
    }
}

@Composable
@ThemeAndLocalePreviews
private fun SearchByActorContentPreview() {
    AflamiTheme {
        MovieContent(
            MovieDetailsUiState(posterUrl = "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg"),
            interactionListener = object : MovieDetailsInteractionListener {
                override fun onMovieExtrasClicked(movieExtras: MovieExtras) {
                }

                override fun onShowAllCastClicked() {
                }

                override fun onBackClicked() {
                }

                override fun onRetryQuestClicked() {
                }
            })
    }
}

