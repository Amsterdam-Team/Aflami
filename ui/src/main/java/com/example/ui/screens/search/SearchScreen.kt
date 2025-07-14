package com.example.ui.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.NoDataContainer
import com.example.designsystem.components.NoNetworkContainer
import com.example.designsystem.components.TabsLayout
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AppTheme
import com.example.ui.application.LocalNavController
import com.example.ui.navigation.Route
import com.example.ui.screens.search.sections.FilterDialog
import com.example.ui.screens.search.sections.RecentSearchesSection
import com.example.ui.screens.search.sections.SuggestionsHubSection
import com.example.ui.screens.searchByCountry.Loading
import com.example.viewmodel.common.MediaType
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.search.FilterInteractionListener
import com.example.viewmodel.search.GlobalSearchInteractionListener
import com.example.viewmodel.search.GlobalSearchViewModel
import com.example.viewmodel.search.SearchErrorUiState
import com.example.viewmodel.search.SearchUiEffect
import com.example.viewmodel.search.SearchUiState
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: GlobalSearchViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SearchUiEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                SearchUiEffect.NavigateToActorSearch -> {
                    navController.navigate(Route.SearchByActor)
                }

                SearchUiEffect.NavigateToMovieDetails -> {
                    Log.e("bk", "collect: NavigateToMovieDetails")
                }

                SearchUiEffect.NavigateToWorldSearch -> {
                    navController.navigate(Route.SearchByCountry)
                }

                null -> {}
            }
        }
    }

    SearchContent(state = state, interaction = viewModel, filterInteraction = viewModel)
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun SearchContent(
    state: SearchUiState,
    interaction: GlobalSearchInteractionListener,
    filterInteraction: FilterInteractionListener
) {

    var bottomPadding by remember { mutableStateOf(0.dp) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.color.surface)
            .statusBarsPadding()
    ) {
        DefaultAppBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(R.string.search),
            onNavigateBackClicked = interaction::onNavigateBackClicked
        )

        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                bottomPadding = coordinates.size.height.dp
            }
        ) {
            TextField(
                modifier = Modifier
                    .background(color = AppTheme.color.surface)
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp),
                text = state.query,
                onValueChange = interaction::onTextValuedChanged,
                hintText = stringResource(R.string.search_hint),
                trailingIcon = R.drawable.ic_filter_vertical,
                onTrailingClick = interaction::onFilterButtonClicked,
                isTrailingClickEnabled = state.query.isNotEmpty(),
                maxCharacters = 100,
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        interaction.onSearchActionClicked()
                    }
                ),
                imeAction = ImeAction.Search,
            )
            AnimatedVisibility(!state.query.isEmpty()) {
                TabsLayout(
                    modifier = Modifier.fillMaxWidth(),
                    tabs = listOf(
                        stringResource(R.string.movies),
                        stringResource(R.string.tv_shows)
                    ),
                    selectedIndex = state.selectedTabOption.index,
                    onSelectTab = { index -> interaction.onTabOptionClicked(TabOption.entries[index]) },
                )
            }
        }

        AnimatedVisibility(state.query.isNotEmpty()) {
            if (
                state.errorUiState == null && !state.isLoading &&
                ((state.selectedTabOption == TabOption.MOVIES && state.movies.isEmpty()) ||
                        (state.selectedTabOption == TabOption.TV_SHOWS && state.tvShows.isEmpty()))
            ) {
                NoDataContainer(
                    imageRes = painterResource(R.drawable.placeholder_no_result_found),
                    title = stringResource(R.string.no_search_result),
                    description = stringResource(R.string.no_search_result_description),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPadding / 2)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    items(
                        if (state.selectedTabOption == TabOption.MOVIES) state.movies
                        else state.tvShows,
                    ) { mediaItem ->
                        with(mediaItem) {
                            MovieCard(
                                movieImage = posterImage,
                                movieType = if (mediaType == MediaType.TV_SHOW) stringResource(R.string.tv_shows)
                                else stringResource(R.string.movies),
                                movieYear = yearOfRelease,
                                movieTitle = name,
                                movieRating = rate,
                            )
                        }
                    }
                }
            }
        }

        SuggestionsHubSection(state = state, interaction = interaction)

        RecentSearchesSection(state = state, interaction = interaction)

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            visible = state.isDialogVisible
        ) {
            FilterDialog(
                state = state.filterItemUiState,
                interaction = filterInteraction,
            )
        }

        AnimatedVisibility(state.isLoading) {
            Loading()
        }

        AnimatedVisibility(state.errorUiState == SearchErrorUiState.NoNetworkConnection && state.query.isNotEmpty()) {
            NoNetworkContainer(
                onClickRetry = interaction::onRetryQuestClicked,
            )
        }
    }
}