package com.example.ui.screens.search.countrySearch

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.R
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.application.LocalNavController
import com.example.ui.components.NoNetworkContainer
import com.example.ui.components.appBar.DefaultAppBar
import com.example.ui.navigation.Route.MovieDetails
import com.example.ui.screens.search.countrySearch.components.CountriesDropdownMenu
import com.example.ui.screens.search.countrySearch.components.CountrySearchField
import com.example.ui.screens.search.countrySearch.components.ExploreCountries
import com.example.ui.screens.search.countrySearch.components.MoviesVerticalGrid
import com.example.ui.screens.search.countrySearch.components.NoMoviesFound
import com.example.viewmodel.search.countrySearch.CountryItemUiState
import com.example.viewmodel.search.countrySearch.CountrySearchEffect
import com.example.viewmodel.search.countrySearch.CountrySearchErrorState
import com.example.viewmodel.search.countrySearch.CountrySearchInteractionListener
import com.example.viewmodel.search.countrySearch.CountrySearchUiState
import com.example.viewmodel.search.countrySearch.CountrySearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SearchByCountryScreen(
    viewModel: CountrySearchViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            effect?.let {
                when (effect) {
                    CountrySearchEffect.NavigateBack -> {
                        navController.popBackStack()
                    }

                    CountrySearchEffect.NavigateToMovieDetails -> navController.navigate(
                        MovieDetails(state.selectedMovieId)
                    )
                }
            }
        }
    }

    SearchByCountryContent(
        state = state,
        interactionListener = viewModel,
    )
}

@Composable
private fun SearchByCountryContent(
    state: CountrySearchUiState,
    interactionListener: CountrySearchInteractionListener,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
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
            focusManager = focusManager
        )

        AnimatedVisibility(
            visible = state.isCountriesDropDownVisible,
            enter = slideInVertically() + expandIn(),
            exit = slideOutVertically() + shrinkOut()
        ) {
            CountriesDropdownMenu(
                items = state.suggestedCountries.take(4),
                isVisible = true,
                onItemClicked = interactionListener::onSelectCountry,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.color.profileOverlay)
            )
        }

        AnimatedContent(
            modifier = Modifier.fillMaxSize().weight(1f),
            targetState = state,
            transitionSpec = { fadeIn(tween(1700)) togetherWith fadeOut(tween(1700)) }) { uiState ->
            when {
                uiState.isLoading -> LoadingContainer()
                uiState.keyword.isEmpty() -> ExploreCountries()
                uiState.movies.isEmpty() && !uiState.isLoading -> NoMoviesFound()
                uiState.errorUiState is CountrySearchErrorState.NoNetworkConnection -> {
                    NoNetworkContainer(
                        onClickRetry = interactionListener::onClickRetry,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                else -> MoviesVerticalGrid(movies = uiState.movies, isVisible = true, onMovieClicked = interactionListener::onClickMovieCard)

            }
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun SearchByCriteriaPreview() {
    AflamiTheme {
        SearchByCountryContent(
            state = CountrySearchUiState(),
            interactionListener = object : CountrySearchInteractionListener {
                override fun onChangeSearchKeyword(keyword: String) {}
                override fun onSelectCountry(country: CountryItemUiState) {}
                override fun onClickNavigateBack() {}
                override fun onClickRetry() {}
                override fun onClickMovieCard(movieId: Long) {
                    TODO("Not yet implemented")
                }
            },
        )
    }
}