package com.amsterdam.ui.screens.search.actorSearch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.TextField
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.screens.search.countrySearch.components.MoviesVerticalGrid
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.search.actorSearch.ActorSearchEffect
import com.amsterdam.viewmodel.search.actorSearch.ActorSearchErrorState
import com.amsterdam.viewmodel.search.actorSearch.ActorSearchInteractionListener
import com.amsterdam.viewmodel.search.actorSearch.ActorSearchUiState
import com.amsterdam.viewmodel.search.actorSearch.ActorSearchViewModel
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SearchByActorScreen(
    modifier: Modifier = Modifier,
    viewModel: ActorSearchViewModel = hiltViewModel()
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val movies = uiState.value.movies.collectAsLazyPagingItems()
    val navigationManager = LocalNavManager.current

    LaunchedEffect(movies.loadState) {
        viewModel.onPagingLoadStateChanged(movies.loadState)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ActorSearchEffect.NavigateBack -> navigationManager.navigateUp()
                is ActorSearchEffect.NavigateToDetailsScreen -> {
                    navigationManager.toMovieDetails(effect.movieId)
                }
            }
        }
    }

    SearchByActorContent(
        modifier = modifier,
        state = uiState.value,
        movies = movies,
        interactionListener = viewModel,
    )
}

@Composable
private fun SearchByActorContent(
    state: ActorSearchUiState,
    interactionListener: ActorSearchInteractionListener,
    movies: LazyPagingItems<SearchMediaItemUiState>,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var headerHeight by remember { mutableStateOf(0.dp) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            Modifier
                .onSizeChanged { headerHeight = it.height.dp }
                .padding(horizontal = 16.dp)
        ) {
            DefaultAppBar(
                title = stringResource(com.amsterdam.designsystem.R.string.find_by_actor),
                showNavigateBackButton = true,
                onNavigateBackClicked = interactionListener::onClickNavigateBack
            )
            TextField(
                modifier = Modifier.padding(top = 8.dp),
                text = state.keyword,
                hintText = stringResource(com.amsterdam.designsystem.R.string.find_by_actor_hint),
                onValueChange = { interactionListener.onUserSearchChange(it) },
                imeAction = ImeAction.Search,
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        interactionListener.onUserSearchChange(state.keyword)
                    }
                )
            )
        }

        if (
            state.keyword.isNotBlank() &&
            !state.isLoading &&
            state.error == null &&
            movies.itemCount != 0
        ) {
            MoviesVerticalGrid(
                movies = movies,
                isVisible = true,
                onMovieClicked = interactionListener::onClickMovie
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                CenterOfScreenContainer(headerHeight) {
                    when {
                        state.isLoading -> LoadingContainer(modifier = Modifier)

                        state.error == ActorSearchErrorState.NoNetworkConnection -> {
                            NoNetworkContainer(interactionListener::onClickRetrySearch)
                        }

                        state.keyword.isBlank() -> {
                            NoDataContainer(
                                modifier = Modifier.padding(horizontal = 24.dp),
                                imageRes = painterResource(R.drawable.img_suggestion_magician),
                                title = stringResource(com.amsterdam.designsystem.R.string.find_by_actor),
                                description = stringResource(com.amsterdam.designsystem.R.string.find_by_actor_description)
                            )
                        }

                        movies.itemCount == 0 -> {
                            NoDataContainer(
                                modifier = Modifier.padding(horizontal = 24.dp),
                                imageRes = painterResource(R.drawable.placeholder_no_result_found),
                                title = stringResource(R.string.no_search_result),
                                description = stringResource(R.string.no_search_result_description)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun MovieImage(imageUrl: String) {
    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
    SafeImageView(
        model = imageUrl,
        contentScale = ContentScale.FillBounds,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        safetyLevel = safetyLevel,
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
            movies = emptyFlow<PagingData< SearchMediaItemUiState>>().collectAsLazyPagingItems(),
            interactionListener = object : ActorSearchInteractionListener {
                override fun onUserSearchChange(keyword: String) {}
                override fun onClickNavigateBack() {}
                override fun onClickRetrySearch() {}
                override fun onClickMovie(movieId: Long) {}
                override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {}
            },
        )
    }
}