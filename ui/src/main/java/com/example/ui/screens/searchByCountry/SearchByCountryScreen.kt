package com.example.ui.screens.searchByCountry

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.R
import com.example.designsystem.components.CenterOfScreenContainer
import com.example.designsystem.components.LoadingIndicator
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.NoDataContainer
import com.example.designsystem.components.NoNetworkContainer
import com.example.designsystem.components.Text
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.application.LocalNavController
import com.example.viewmodel.search.countrySearch.CountryUiState
import com.example.viewmodel.search.countrySearch.SearchByCountryContentUIState
import com.example.viewmodel.search.countrySearch.SearchByCountryInteractionListener
import com.example.viewmodel.search.countrySearch.SearchByCountryScreenState
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SearchByCountryScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchByCountryViewModel = koinViewModel()
) {
    val navController = LocalNavController.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchByCountryScreenContent(
        state = state,
        interactionListener = viewModel,
        onNavigateBackClicked = (navController::popBackStack),
        modifier = modifier,
    )
}

@Composable
private fun SearchByCountryScreenContent(
    state: SearchByCountryScreenState,
    interactionListener: SearchByCountryInteractionListener,
    onNavigateBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
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
                title = stringResource(R.string.world_tour_title),
                showNavigateBackButton = true,
                onNavigateBackClicked = onNavigateBackClicked,
            )
            CountrySearchField(state, interactionListener, focusManager)
        }
        Box {
            CenterOfScreenContainer(
                headerHeight,
                modifier = Modifier.fillMaxSize().padding(start = 8.dp, end = 8.dp),
            ) {
                when (state.searchByCountryContentUIState) {
                    SearchByCountryContentUIState.COUNTRY_TOUR -> ExploreCountries()
                    SearchByCountryContentUIState.LOADING_MOVIES -> Loading()
                    SearchByCountryContentUIState.NO_INTERNET_CONNECTION -> NoInternetConnection(
                        onRetryQuestClicked = interactionListener::onRetryQuestClicked,
                    )
                    SearchByCountryContentUIState.NO_DATA_FOUND -> NoMoviesFound()
                    else -> {}
                }
            }
            SearchedMovies(state, Modifier.align(Alignment.TopStart))
            CountriesDropdownMenu(
                items = state.suggestedCountries.take(5),
                isVisible = state.isCountriesDropDownVisible,
                onItemClicked = (interactionListener::onCountrySelected),
                modifier = Modifier.fillMaxWidth().background(AppTheme.color.profileOverlay)
            )
        }
    }
}

@Composable
private fun CountrySearchField(
    state: SearchByCountryScreenState,
    interactionListener: SearchByCountryInteractionListener,
    focusManager: FocusManager
) {
    TextField(
        text = state.keyword,
        hintText = stringResource(R.string.country_name_hint),
        onValueChange = { interactionListener.onKeywordValueChanged(it) },
        keyboardActions = KeyboardActions(
            onDone = {
                interactionListener.onKeywordValueChanged(state.keyword)
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
private fun CountriesDropdownMenu(
    items: List<CountryUiState>,
    onItemClicked: (CountryUiState) -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    androidx.compose.animation.AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = modifier
                .border(
                    0.5.dp,
                    AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp),
                )
                .background(AppTheme.color.surface)
                .padding(vertical = 6.dp)
        ) {
            items.forEach { item ->
                Text(
                    text = item.countryName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            focusManager.clearFocus()
                            onItemClicked(item)
                        },
                    style = AppTheme.textStyle.body.small,
                    color = AppTheme.color.body
                )
            }
        }
    }
}

@Composable
private fun ExploreCountries(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.tour_world_image),
            contentDescription = stringResource(R.string.country_tour_image_description),
            modifier = Modifier.height(82.dp)
        )
        Text(
            text = stringResource(R.string.country_tour_title),
            modifier = Modifier.padding(top = 16.dp),
            style = AppTheme.textStyle.title.medium,
            color = AppTheme.color.title,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.country_tour_description),
            modifier = Modifier.padding(top = 8.dp),
            style = AppTheme.textStyle.body.small,
            color = AppTheme.color.body,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun Loading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingIndicator()
        Text(
            text = stringResource(R.string.loading),
            style = AppTheme.textStyle.label.medium,
            color = AppTheme.color.body,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun NoInternetConnection(
    onRetryQuestClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NoNetworkContainer(
        onClickRetry = onRetryQuestClicked,
        modifier = modifier.padding(vertical = 8.dp).verticalScroll(rememberScrollState()),
    )
}

@Composable
private fun NoMoviesFound(
    modifier: Modifier = Modifier
) {
    NoDataContainer(
        modifier = modifier.padding(vertical = 8.dp).verticalScroll(rememberScrollState()),
        title = stringResource(R.string.no_search_result),
        description = stringResource(R.string.no_search_result_for_country),
        imageRes = painterResource(id = R.drawable.placeholder_no_result_found),
    )
}

@Composable
private fun SearchedMovies(
    state: SearchByCountryScreenState,
    modifier: Modifier = Modifier
) {
    androidx.compose.animation.AnimatedVisibility(
        state.searchByCountryContentUIState == SearchByCountryContentUIState.MOVIES_LOADED
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(160.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 4.dp),
        ) {
            items(
                items = state.movies,
                key = { movie -> movie.id }
            ) { movie ->
                MovieCard(
                    movieImage = movie.poster,
                    movieType = stringResource(R.string.movie),
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

@Composable
@ThemeAndLocalePreviews
private fun SearchByCriteriaPreview() {
    AflamiTheme {
        SearchByCountryScreenContent(
            state = SearchByCountryScreenState(),
            onNavigateBackClicked = {},
            interactionListener = object : SearchByCountryInteractionListener {
                override fun onKeywordValueChanged(keyword: String) {}
                override fun onCountrySelected(country: CountryUiState) {}
                override fun onRetryQuestClicked() {}
            },
        )
    }
}