package com.example.ui.screens.search.actorSearch

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.designsystem.components.ImageErrorIndicator
import com.example.designsystem.components.ImageLoadingIndicator
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.components.TextField
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.application.LocalNavController
import com.example.ui.components.NoDataContainer
import com.example.ui.components.NoNetworkContainer
import com.example.ui.navigation.Route
import com.example.viewmodel.search.actorSearch.SearchActorEffect
import com.example.viewmodel.search.actorSearch.SearchActorInteractionListener
import com.example.ui.R
import com.example.ui.components.MovieCard
import com.example.ui.components.appBar.DefaultAppBar
import com.example.ui.utils.safeNavigate
import com.example.viewmodel.search.actorSearch.ActorSearchUiState
import com.example.viewmodel.search.actorSearch.SearchActorViewModel
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import kotlinx.coroutines.flow.emptyFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchByActorScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchActorViewModel = koinViewModel(),
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val moviesFlow = uiState.value.movies.collectAsLazyPagingItems()
    val navController = LocalNavController.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            effect?.let {
                when (it) {
                    SearchActorEffect.NavigateBack -> navController.popBackStack()
                    is SearchActorEffect.NavigateToDetailsScreen -> {
                        viewModel.onSaveSearchHistory()
                        navController.safeNavigate(Route.MovieDetails(it.movieId))
                    }
                }
            }
        }
    }
    SearchByActorContent(
        modifier = modifier,
        state = uiState.value,
        moviesFlow = moviesFlow,
        interactionListener = viewModel,
    )
}

@Composable
private fun SearchByActorContent(
    state: ActorSearchUiState,
    interactionListener: SearchActorInteractionListener,
    moviesFlow: LazyPagingItems<MovieItemUiState>,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .statusBarsPadding(),
    ) {
        DefaultAppBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(com.example.designsystem.R.string.find_by_actor),
            showNavigateBackButton = true,
            onNavigateBackClicked = interactionListener::onClickNavigateBack
        )
        TextField(
            text = state.keyword,
            hintText = stringResource(com.example.designsystem.R.string.find_by_actor),
            onValueChange = { interactionListener.onUserSearchChange(it) },
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    interactionListener.onUserSearchChange(state.keyword)
                    interactionListener.onSaveSearchHistory()
                },
            ),
            imeAction = ImeAction.Search,
            modifier =
                Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp),
        )

        AnimatedContent(
            targetState = state,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
            },
            label = "Content Animation",
        ) { targetState ->
            when {
                targetState.isLoading ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingContainer(modifier = Modifier)
                    }

                targetState.error != null -> {
                    if (targetState.error == ActorSearchUiState.SearchByActorError.NetworkError) {
                        NoNetworkContainer(
                            onClickRetry = interactionListener::onClickRetrySearch,
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .align(Alignment.CenterHorizontally),
                        )
                    }
                }

                targetState.keyword.isBlank() -> {
                    NoDataContainer(
                        imageRes = painterResource(R.drawable.img_suggestion_magician),
                        title = stringResource(com.example.designsystem.R.string.find_by_actor),
                        description = stringResource(com.example.designsystem.R.string.find_by_actor_description),
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 144.dp)
                    )
                }
                moviesFlow.loadState.refresh is LoadState.Loading -> LoadingContainer()

                moviesFlow.itemSnapshotList.isEmpty() -> {
                    NoDataContainer(
                        imageRes = painterResource(R.drawable.placeholder_no_result_found),
                        title = stringResource(R.string.no_search_result),
                        description = stringResource(R.string.no_search_result_description),
                        modifier =
                            Modifier
                                .padding(horizontal = 24.dp)
                                .padding(top = 144.dp),
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(160.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(
                            vertical = 12.dp,
                            horizontal = 16.dp
                        ),
                    ) {
                        items(
                            count = moviesFlow.itemCount,
                            key = { index -> "${moviesFlow[index]?.id}-$index" },
                        ) { index ->
                            val movie = moviesFlow[index] ?: return@items
                            MovieCard(
                                movieImage = { MovieImage(movie.posterImageUrl) },
                                movieType = stringResource(R.string.movie),
                                movieYear = movie.yearOfRelease,
                                movieTitle = movie.name,
                                movieRating = movie.rate,
                            ) {
                                interactionListener.onClickMovie(movie.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MovieImage(imageUrl: String) {
    SafeImageView(
        model = imageUrl,
        contentScale = ContentScale.FillBounds,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        onLoading = { ImageLoadingIndicator() },
        onError = { ImageErrorIndicator() },
    )
}

@Composable
@ThemeAndLocalePreviews
private fun SearchByActorContentPreview() {
    AflamiTheme {
        SearchByActorContent(
            state = ActorSearchUiState(),
            moviesFlow = emptyFlow<PagingData<MovieItemUiState>>().collectAsLazyPagingItems(),
            interactionListener = object : SearchActorInteractionListener {
                override fun onUserSearchChange(query: String) {}
                override fun onClickNavigateBack() {}
                override fun onClickRetrySearch() {}
                override fun onClickMovie(movieId: Long) {}
                override fun onSaveSearchHistory() {}
            },
        )
    }
}