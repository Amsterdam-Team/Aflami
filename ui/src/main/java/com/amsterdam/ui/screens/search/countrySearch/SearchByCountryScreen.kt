package com.amsterdam.ui.screens.search.countrySearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route.MovieDetails
import com.amsterdam.ui.screens.search.countrySearch.components.CountriesDropdownMenu
import com.amsterdam.ui.screens.search.countrySearch.components.CountrySearchField
import com.amsterdam.ui.screens.search.countrySearch.components.ExploreCountries
import com.amsterdam.ui.screens.search.countrySearch.components.MoviesVerticalGrid
import com.amsterdam.ui.screens.search.countrySearch.components.NoMoviesFound
import com.amsterdam.ui.utils.safeNavigate
import com.amsterdam.viewmodel.search.countrySearch.CountryItemUiState
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchEffect
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchErrorState
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchInteractionListener
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchUiState
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun SearchByCountryScreen(
    viewModel: CountrySearchViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val movies = state.movies.collectAsLazyPagingItems()
    LaunchedEffect(movies.loadState) {
        viewModel.onPagingLoadStateChanged(movies.loadState)
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            effect?.let {
                when (effect) {
                    CountrySearchEffect.NavigateBack -> {
                        navController.popBackStack()
                    }

                    CountrySearchEffect.NavigateToMovieDetails -> navController.safeNavigate(
                        MovieDetails(state.selectedMovieId)
                    )
                }
            }
        }
    }

    SearchByCountryContent(
        state = state,
        movies = movies,
        interactionListener = viewModel,
    )
}

@Composable
private fun SearchByCountryContent(
    state: CountrySearchUiState,
    movies: LazyPagingItems<MovieItemUiState>,
    interactionListener: CountrySearchInteractionListener,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        var headerHeight by remember { mutableStateOf(0.dp) }
        Column(Modifier.onSizeChanged { headerHeight = it.height.dp }) {
            DefaultAppBar(
                title = stringResource(R.string.world_tour_title),
                showNavigateBackButton = true,
                onNavigateBackClicked = { interactionListener.onClickNavigateBack() },
            )
            CountrySearchField(
                keyword = state.keyword,
                onKeywordValueChanged = interactionListener::onChangeSearchKeyword,
            )
        }

        Box {
            if (
                state.keyword.isNotBlank() &&
                !state.isLoading &&
                state.errorUiState == null &&
                movies.itemCount != 0 &&
                !state.showSuggestedCountries
            ) {
                MoviesVerticalGrid(
                    movies = movies,
                    isVisible = true,
                    onMovieClicked = interactionListener::onClickMovieCard
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp)
                ) {
                    CenterOfScreenContainer(unneededSpace = headerHeight) {
                        when {
                            state.isLoading -> LoadingContainer()
                            state.errorUiState is CountrySearchErrorState.NoNetworkConnection -> {
                                NoNetworkContainer(
                                    onClickRetry = interactionListener::onClickRetry,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            movies.itemCount == 0 &&
                                    !state.showSuggestedCountries &&
                                    !state.isLoading &&
                                    state.selectedCountryIsoCode.isNotBlank() -> NoMoviesFound()

                            movies.itemCount == 0 -> ExploreCountries()

                            else -> {}
                        }
                    }
                }
            }
            CountriesDropdownMenu(
                items = state.suggestedCountries.take(4),
                isVisible = state.showSuggestedCountries && state.suggestedCountries.isNotEmpty(),
                onItemClicked = interactionListener::onSelectCountry,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.color.profileOverlay)
            )
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun SearchByCriteriaPreview() {
    AflamiTheme {
        SearchByCountryContent(
            state = CountrySearchUiState(),
            movies = emptyFlow<PagingData<MovieItemUiState>>().collectAsLazyPagingItems(),
            interactionListener = object : CountrySearchInteractionListener {
                override fun onChangeSearchKeyword(keyword: String) {}
                override fun onSelectCountry(country: CountryItemUiState) {}
                override fun onClickNavigateBack() {}
                override fun onClickRetry() {}
                override fun onClickMovieCard(movieId: Long) {}
                override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {}
            },
        )
    }
}