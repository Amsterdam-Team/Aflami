package com.example.ui.screens.searchByActor

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.R
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.NoDataContainer
import com.example.designsystem.components.NoNetworkContainer
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.application.LocalNavController
import com.example.ui.screens.searchByCountry.Loading
import com.example.viewmodel.search.actorSearch.SearchByActorEffect
import com.example.viewmodel.search.actorSearch.SearchByActorInteractionListener
import com.example.viewmodel.search.actorSearch.SearchByActorScreenState
import com.example.viewmodel.search.actorSearch.SearchByActorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchByActorScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchByActorViewModel = koinViewModel(),
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    var isNoInternetConnection by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                SearchByActorEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                SearchByActorEffect.NoInternetConnection -> {
                    isNoInternetConnection = true
                }
                null -> {}
            }
        }
    }
    SearchByActorContent(
        modifier = modifier,
        state = uiState.value,
        interactionListener = viewModel,
        isNoInternetConnection = isNoInternetConnection,
        onRetryQuestClicked = {
            isNoInternetConnection = false
            viewModel.onRetryQuestClicked()
        }
    )
}

@Composable
private fun SearchByActorContent(
    modifier: Modifier = Modifier,
    state: SearchByActorScreenState,
    interactionListener: SearchByActorInteractionListener,
    isNoInternetConnection: Boolean,
    onRetryQuestClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        DefaultAppBar(
            title = stringResource(R.string.find_by_actor),
            showNavigateBackButton = true,
            onNavigateBackClicked = { interactionListener.onNavigateBackClicked() }
        )
        TextField(
            text = state.query,
            hintText = stringResource(R.string.find_by_actor),
            onValueChange = { interactionListener.onKeywordValueChanged(it) },

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
                targetState.isLoading -> Loading(modifier = Modifier)

                isNoInternetConnection -> {
                    NoNetworkContainer(
                        onClickRetry = onRetryQuestClicked,
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .align(Alignment.CenterHorizontally)
                    )
                }

                targetState.query.isBlank() -> {
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
                        modifier = Modifier.padding(horizontal = 16.dp),
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(targetState.movies) { movie ->
                            MovieCard(
                                movieImage = movie.poster,
                                movieType = "Movies",
                                movieYear = movie.productionYear,
                                movieTitle = movie.name,
                                movieRating = movie.rating,
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.navigationBarsPadding())
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
            state = SearchByActorScreenState(),
            interactionListener = object : SearchByActorInteractionListener {
                override fun onKeywordValueChanged(keyword: String) {
                }

                override fun onNavigateBackClicked() {
                }

                override fun onRetryQuestClicked() {
                }
            },
            isNoInternetConnection = false,
            onRetryQuestClicked = {}
        )
    }
}