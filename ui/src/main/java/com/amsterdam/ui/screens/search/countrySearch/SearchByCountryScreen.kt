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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.screens.search.countrySearch.components.CountriesDropdownMenu
import com.amsterdam.ui.screens.search.countrySearch.components.CountrySearchField
import com.amsterdam.ui.screens.search.countrySearch.components.ExploreCountries
import com.amsterdam.ui.screens.search.countrySearch.components.MoviesVerticalGrid
import com.amsterdam.ui.screens.search.countrySearch.components.NoMoviesFound
import com.amsterdam.viewmodel.search.countrySearch.CountryItemUiState
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchEffect
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchInteractionListener
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchUiState
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchViewModel
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.NoInternetError
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun SearchByCountryScreen(
    viewModel: CountrySearchViewModel = hiltViewModel()
) {
    val navigationManager = LocalNavManager.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorUiState by viewModel.errorState.collectAsStateWithLifecycle()
    val movies = state.movies.collectAsLazyPagingItems()
    LaunchedEffect(movies.loadState) {
        viewModel.onPagingLoadStateChanged(movies.loadState)
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CountrySearchEffect.NavigateBack -> {
                    navigationManager.navigateUp()
                }

                CountrySearchEffect.NavigateToMovieDetails -> navigationManager.toMovieDetails(state.selectedMovieId)
            }
        }
    }

    SearchByCountryContent(
        state = state,
        errorState = errorUiState,
        movies = movies,
        interactionListener = viewModel,
    )
}

@Composable
private fun SearchByCountryContent(
    state: CountrySearchUiState,
    errorState: ErrorUiState?,
    movies: LazyPagingItems<SearchMediaItemUiState>,
    interactionListener: CountrySearchInteractionListener,
) {
    val density = LocalDensity.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        var headerHeight by remember { mutableStateOf(0.dp) }
        Column(
            Modifier
                .onSizeChanged { headerHeight = with(density) { it.height.dp } }
                .padding(horizontal = 16.dp)
        ) {
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
                errorState == null &&
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
                            errorState is NoInternetError -> {
                                NoNetworkContainer(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    onClickRetry = interactionListener::onClickRetry
                                )
                            }

                            movies.itemCount == 0 &&
                                    !state.showSuggestedCountries &&
                                    !state.isLoading &&
                                    state.selectedCountryIsoCode.isNotBlank() -> NoMoviesFound()

                            movies.itemCount == 0 -> ExploreCountries(Modifier.padding(horizontal = 16.dp))

                            else -> {}
                        }
                    }
                }
            }
            CountriesDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(AppTheme.color.profileOverlay),
                items = state.suggestedCountries.take(4),
                isVisible = state.showSuggestedCountries && state.suggestedCountries.isNotEmpty(),
                onItemClicked = interactionListener::onSelectCountry
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
            errorState = null,
            movies = emptyFlow<PagingData< SearchMediaItemUiState>>().collectAsLazyPagingItems(),
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