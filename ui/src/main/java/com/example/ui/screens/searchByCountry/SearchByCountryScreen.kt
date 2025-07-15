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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import com.example.viewmodel.search.countrySearch.SearchByCountryEffect
import com.example.viewmodel.search.countrySearch.SearchByCountryInteractionListener
import com.example.viewmodel.search.countrySearch.SearchByCountryScreenState
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchByCountryScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchByCountryViewModel = koinViewModel()
) {
    val navController = LocalNavController.current

    val state by viewModel.state.collectAsState()
    var screenContent by rememberSaveable { mutableStateOf(ScreenContent.COUNTRY_TOUR) }
    var showCountriesDropdown by remember { mutableStateOf(false) }
    var noSuggestedCountry by remember { mutableStateOf(false) }

    LaunchedEffect(
        viewModel.effect
    ) {
        viewModel.effect.collectLatest {
            when (it) {
                SearchByCountryEffect.NoInternetConnectionEffect -> {
                    screenContent = ScreenContent.NO_INTERNET_CONNECTION
                }

                SearchByCountryEffect.LoadingMoviesEffect -> {
                    screenContent = ScreenContent.LOADING_MOVIES
                }

                SearchByCountryEffect.MoviesLoadedEffect -> {
                    screenContent = ScreenContent.MOVIES
                }

                SearchByCountryEffect.NoMoviesEffect -> {
                    ScreenContent.NO_MOVIES
                }

                SearchByCountryEffect.LoadingSuggestedCountriesEffect -> {
                    showCountriesDropdown = true
                    noSuggestedCountry = false
                }

                SearchByCountryEffect.NoSuggestedCountriesEffect -> {
                    noSuggestedCountry = true
                    showCountriesDropdown = false
                }

                SearchByCountryEffect.ShowCountriesDropDown -> {
                    showCountriesDropdown = true
                }

                SearchByCountryEffect.HideCountriesDropDown -> {
                    showCountriesDropdown = false
                }

                SearchByCountryEffect.CountryTooShortEffect -> {}
                else -> {}
            }
        }
    }

    SearchByCountryScreenContent(
        state = state,
        interactionListener = viewModel,
        onNavigateBackClicked = {
            navController.popBackStack()
        },
        modifier = modifier,
        screenContent = screenContent,
        showCountriesDropdown = showCountriesDropdown,
    )
}

@Composable
fun SearchByCountryScreenContent(
    state: SearchByCountryScreenState,
    interactionListener: SearchByCountryInteractionListener,
    onNavigateBackClicked: () -> Unit,
    screenContent: ScreenContent,
    showCountriesDropdown: Boolean,
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
        Column(
            modifier = Modifier
                .onSizeChanged {
                    headerHeight = it.height.dp
                }
        ) {
            DefaultAppBar(
                title = stringResource(R.string.world_tour_title),
                showNavigateBackButton = true,
                onNavigateBackClicked = onNavigateBackClicked,
            )

            TextField(
                text = state.selectedCountry,
                hintText = stringResource(R.string.country_name_hint),
                onValueChange = { interactionListener.onCountryNameUpdated(it) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        interactionListener.onCountryNameUpdated(state.selectedCountry)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
        Box {
            CenterOfScreenContainer(
                headerHeight,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp),
            ) {
                when (screenContent) {
                    ScreenContent.COUNTRY_TOUR -> ExploreCountries()
                    ScreenContent.LOADING_MOVIES -> Loading()
                    ScreenContent.NO_INTERNET_CONNECTION -> NoInternetConnection(
                        onRetryQuestClicked = interactionListener::onRetryQuestClicked,
                    )
                    ScreenContent.NO_MOVIES -> NoMoviesFound()
                    else -> {}
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                screenContent == ScreenContent.MOVIES
            ) {
                SearchedMovies(
                    state,
                    Modifier.align(Alignment.TopStart)
                )
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = showCountriesDropdown,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                CountriesDropdownMenu(
                    items = state.suggestedCountries.take(5),
                    onItemClicked = {
                        interactionListener.onSelectCountry(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.color.profileOverlay)
                )
            }
        }
    }
}

@Composable
private fun CountriesDropdownMenu(
    items: List<CountryUiState>,
    onItemClicked: (CountryUiState) -> Unit,
    modifier: Modifier = Modifier
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
                        onItemClicked(item)
                    },
                style = AppTheme.textStyle.body.small,
                color = AppTheme.color.body
            )
        }
    }
}

@Composable
private fun ExploreCountries(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            Image(
                painter = painterResource(R.drawable.tour_world_image),
                contentDescription = stringResource(R.string.country_tour_image_description),
                modifier = Modifier.height(82.dp)
            )
        }
        item {
            Text(
                text = stringResource(R.string.country_tour_title),
                modifier = Modifier.padding(top = 16.dp),
                style = AppTheme.textStyle.title.medium,
                color = AppTheme.color.title,
                textAlign = TextAlign.Center
            )
        }
        item {
            Text(
                text = stringResource(R.string.country_tour_description),
                modifier = Modifier.padding(top = 8.dp),
                style = AppTheme.textStyle.body.small,
                color = AppTheme.color.body,
                textAlign = TextAlign.Center
            )
        }
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
private fun NoInternetConnection(
    onRetryQuestClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NoNetworkContainer(onClickRetry = onRetryQuestClicked, modifier = modifier)
}

@Composable
private fun NoMoviesFound(
    modifier: Modifier = Modifier
) {
    NoDataContainer(
        modifier = modifier,
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

enum class ScreenContent {
    COUNTRY_TOUR,
    LOADING_MOVIES,
    NO_INTERNET_CONNECTION,
    NO_MOVIES,
    MOVIES
}

@Composable
@ThemeAndLocalePreviews
private fun SearchByCriteriaPreview() {
    AflamiTheme {
        SearchByCountryScreenContent(
            state = SearchByCountryScreenState(),
            interactionListener = object : SearchByCountryInteractionListener {
                override fun onCountryNameUpdated(countryName: String) {
                }

                override fun onSelectCountry(country: CountryUiState) {
                }

                override fun onRetryQuestClicked() {
                }
            },
            showCountriesDropdown = false,
            onNavigateBackClicked = {},
            screenContent = ScreenContent.COUNTRY_TOUR
        )
    }
}