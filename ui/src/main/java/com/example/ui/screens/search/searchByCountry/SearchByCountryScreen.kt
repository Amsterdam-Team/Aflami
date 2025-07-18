package com.example.ui.screens.search.searchByCountry

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.R
import com.example.designsystem.components.CenterOfScreenContainer
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.application.LocalNavController
import com.example.ui.components.NoNetworkContainer
import com.example.ui.components.appBar.DefaultAppBar
import com.example.ui.navigation.Route
import com.example.ui.screens.search.searchByCountry.components.CountriesDropdownMenu
import com.example.ui.screens.search.searchByCountry.components.CountrySearchField
import com.example.ui.screens.search.searchByCountry.components.ExploreCountries
import com.example.ui.screens.search.searchByCountry.components.MoviesVerticalGrid
import com.example.ui.screens.search.searchByCountry.components.NoMoviesFound
import com.example.viewmodel.search.countrySearch.CountryUiState
import com.example.viewmodel.search.countrySearch.SearchByCountryContentUIState
import com.example.viewmodel.search.countrySearch.SearchByCountryEffect
import com.example.viewmodel.search.countrySearch.SearchByCountryInteractionListener
import com.example.viewmodel.search.countrySearch.SearchByCountryScreenState
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SearchByCountryScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchByCountryViewModel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SearchByCountryEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                SearchByCountryEffect.NavigateToMovieDetails ->
                    navController.navigate(Route.MovieDetails(state.selectedMovieId))
                else -> {}
            }
        }
    }
    SearchByCountryScreenContent(
        state = state,
        interactionListener = viewModel,
        modifier = modifier,
    )
}

@Composable
private fun SearchByCountryScreenContent(
    state: SearchByCountryScreenState,
    interactionListener: SearchByCountryInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp),
    ) {
        var headerHeight by remember { mutableStateOf(0.dp) }
        val focusManager = LocalFocusManager.current
        Column(modifier = Modifier.onSizeChanged { headerHeight = it.height.dp }) {
            DefaultAppBar(
                title = stringResource(R.string.world_tour_title),
                showNavigateBackButton = true,
                onNavigateBackClicked = { interactionListener.onNavigateBackClicked() },
            )
            CountrySearchField(state.keyword, interactionListener::onKeywordValueChanged, focusManager)
        }
        Box {
            CenterOfScreenContainer(
                headerHeight,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            ) {
                when (state.searchByCountryContentUIState) {
                    SearchByCountryContentUIState.COUNTRY_TOUR -> ExploreCountries()
                    SearchByCountryContentUIState.LOADING_MOVIES -> LoadingContainer()
                    SearchByCountryContentUIState.NO_INTERNET_CONNECTION ->
                        NoNetworkContainer(
                            onClickRetry = interactionListener::onRetryRequestClicked,
                            modifier = modifier.padding(vertical = 8.dp),
                        )

                    SearchByCountryContentUIState.NO_DATA_FOUND -> NoMoviesFound()
                    else -> {}
                }
            }
            MoviesVerticalGrid(
                state.movies,
                state.searchByCountryContentUIState == SearchByCountryContentUIState.MOVIES_LOADED,
                Modifier.align(Alignment.TopStart),
                interactionListener::onMovieClicked,
            )
            CountriesDropdownMenu(
                items = state.suggestedCountries.take(4),
                isVisible = state.isCountriesDropDownVisible,
                onItemClicked = (interactionListener::onCountrySelected),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(AppTheme.color.profileOverlay),
            )
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun SearchByCriteriaPreview() {
    AflamiTheme {
        SearchByCountryScreenContent(
            state = SearchByCountryScreenState(),
            interactionListener =
                object : SearchByCountryInteractionListener {
                    override fun onKeywordValueChanged(keyword: String) {}

                    override fun onCountrySelected(country: CountryUiState) {}

                    override fun onNavigateBackClicked() {}

                    override fun onRetryRequestClicked() {}

                    override fun onMovieClicked(movieId: Long) {
                    }
                },
        )
    }
}
