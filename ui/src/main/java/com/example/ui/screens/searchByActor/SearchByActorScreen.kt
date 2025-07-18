package com.example.ui.screens.searchByActor

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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.R
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.NoDataContainer
import com.example.designsystem.components.NoNetworkContainer
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.application.LocalNavController
import com.example.ui.navigation.Route
import com.example.viewmodel.search.actorSearch.SearchActorEffect
import com.example.viewmodel.search.actorSearch.SearchActorInteractionListener

import com.example.viewmodel.search.actorSearch.ActorSearchUiState
import com.example.viewmodel.search.actorSearch.SearchActorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchByActorScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchActorViewModel = koinViewModel(),
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                SearchActorEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                SearchActorEffect.NavigateToDetailsScreen ->
                    navController.navigate(Route.MovieDetails(uiState.value.selectedMovieId))
                null -> {}
            }
        }
    }
    SearchByActorContent(
        modifier = modifier,
        state = uiState.value,
        interactionListener = viewModel
    )
}

@Composable
private fun SearchByActorContent(
    modifier: Modifier = Modifier,
    state: ActorSearchUiState,
    interactionListener: SearchActorInteractionListener,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        DefaultAppBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(R.string.find_by_actor),
            showNavigateBackButton = true,
            onNavigateBackClicked = interactionListener::onClickNavigateBack
        )
        TextField(
            text = state.keyword,
            hintText = stringResource(R.string.find_by_actor),
            onValueChange = { interactionListener.onUserSearchChange(it) },
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
        )

        AnimatedContent(
            targetState = state,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300))
            },
            label = "Content Animation"
        ) { targetState ->
            when {
                targetState.isLoading ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingContainer(modifier = Modifier)
                    }
                targetState.error != null -> {
                    when(targetState.error) {
                        ActorSearchUiState.SearchByActorError.NetworkError ->
                            NoNetworkContainer(
                            onClickRetry = interactionListener::onClickRetrySearch,
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .align(Alignment.CenterHorizontally)
                        )
                        null -> {}
                    }

                }

                targetState.keyword.isBlank() -> {
                    NoDataContainer(
                        imageRes = painterResource(R.drawable.img_suggestion_magician),
                        title = stringResource(R.string.find_by_actor),
                        description = stringResource(R.string.find_by_actor_description),
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 144.dp)
                    )
                }

                targetState.movies.isEmpty() -> {
                    NoDataContainer(
                        imageRes = painterResource(R.drawable.placeholder_no_result_found),
                        title = stringResource(R.string.no_search_result),
                        description = stringResource(R.string.no_search_result_description),
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 144.dp)
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(160.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                    ) {
                        items(targetState.movies) { movie ->
                            MovieCard(
                                movieImage = movie.poster,
                                movieType = "Movies",
                                movieYear = movie.productionYear,
                                movieTitle = movie.name,
                                movieRating = movie.rating,
                            ){
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
@ThemeAndLocalePreviews
private fun SearchByActorContentPreview() {
    AflamiTheme {
        SearchByActorContent(
            state = ActorSearchUiState(),
            interactionListener = object : SearchActorInteractionListener {
                override fun onUserSearchChange(query: String) {
                }

                override fun onClickNavigateBack() {
                }

                override fun onClickRetrySearch() {
                }

                override fun onClickMovie(movieId: Long) {
                }
            }
        )
    }
}