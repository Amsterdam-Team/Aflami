package com.example.ui.screens.searchByActor

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.R
import com.example.designsystem.components.CenterOfScreenContainer
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
                null -> {}
            }
        }
    }
    SearchByActorContent(
        modifier = modifier,
        state = uiState.value,
        interactionListener = viewModel,
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
    onRetryQuestClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        var headerHeight by remember { mutableStateOf(0.dp) }
        val focusManager = LocalFocusManager.current
        Column(modifier = Modifier.onSizeChanged { headerHeight = it.height.dp }) {
            DefaultAppBar(
                title = stringResource(R.string.find_by_actor),
                showNavigateBackButton = true,
                onNavigateBackClicked = { interactionListener.onNavigateBackClicked() }
            )
            TextField(
                text = state.keyword,
                hintText = stringResource(R.string.find_by_actor),
                onValueChange = { interactionListener.onKeywordValueChanged(it) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        interactionListener.onKeywordValueChanged(state.keyword)
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        AnimatedContent(
            targetState = state,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300))
            },
        ) { targetState ->
            CenterOfScreenContainer(
                unneededSpace = headerHeight,
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            ) {
                when {
                    targetState.isLoading -> Loading(modifier = Modifier)
                    targetState.noInternetException -> NoNetworkContainer(
                        onClickRetry = onRetryQuestClicked,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    targetState.keyword.isBlank() -> {
                        NoDataContainer(
                            imageRes = painterResource(R.drawable.img_suggestion_magician),
                            title = stringResource(R.string.find_by_actor),
                            description = stringResource(R.string.find_by_actor_description),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                    targetState.movies.isEmpty() -> {
                        NoDataContainer(
                            imageRes = painterResource(R.drawable.placeholder_no_result_found),
                            title = stringResource(R.string.no_search_result),
                            description = stringResource(R.string.no_search_result_description),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                    else -> {}
                }
            }
            if (targetState.movies.isNotEmpty() && !targetState.isLoading) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                ) {
                    items(targetState.movies) { movie ->
                        MovieCard(
                            movieImage = movie.poster,
                            movieType = stringResource(R.string.movie),
                            movieYear = movie.productionYear,
                            movieTitle = movie.name,
                            movieRating = movie.rating,
                        )
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
                override fun onKeywordValueChanged(keyword: String) {}
                override fun onNavigateBackClicked() {}
                override fun onRetryQuestClicked() {}
            },
            onRetryQuestClicked = {}
        )
    }
}