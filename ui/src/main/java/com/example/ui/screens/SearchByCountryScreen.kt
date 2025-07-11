package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.NoDataContainer
import com.example.designsystem.components.Text
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.viewmodel.search.countrySearch.SearchByCountryEffect
import com.example.viewmodel.search.countrySearch.SearchByCountryUiState
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchByCountryScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchByCountryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var screenContent by remember { mutableStateOf(ScreenContent.COUNTRY_TOUR) }
    var showLoadingCountries by remember { mutableStateOf(false) }

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

                SearchByCountryEffect.LoadingSuggestedCountriesEffect -> {
                    showLoadingCountries = true
                }

                SearchByCountryEffect.MoviesLoadedEffect -> {
                    screenContent = ScreenContent.MOVIES
                }

                SearchByCountryEffect.NoMoviesEffect -> {
                    ScreenContent.NO_MOVIES
                }

                SearchByCountryEffect.NoSuggestedCountriesEffect -> {
                    showLoadingCountries = false
                }

                SearchByCountryEffect.SuggestedCountriesLoadedEffect -> {
                    showLoadingCountries = false
                }

                SearchByCountryEffect.InitialEffect -> {
                    screenContent = ScreenContent.COUNTRY_TOUR
                }

                SearchByCountryEffect.CountryTooShortEffect -> {}
            }
        }
    }

    SearchByCountryScreenContent(
        state,
        viewModel::onCountryNameUpdated,
        modifier,
        screenContent,
        showLoadingCountries
    )
}

@Composable
fun SearchByCountryScreenContent(
    state: SearchByCountryUiState,
    onCountryNameUpdated: (String) -> Unit,
    modifier: Modifier = Modifier,
    screenContent: ScreenContent,
    showLoadingCountries: Boolean,
) {
    Column {
        DefaultAppBar(
            title = stringResource(R.string.world_tour_title),
            showNavigateBackButton = true,
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            TextField(
                state.selectedCountry,
                hintText = stringResource(R.string.country_name_hint),
                onValueChange = { onCountryNameUpdated(it) }
            )

            when (screenContent) {
                ScreenContent.COUNTRY_TOUR -> ExploreCountries()
                ScreenContent.LOADING_MOVIES -> LoadingMovies()
                ScreenContent.NO_INTERNET_CONNECTION -> NoInternetConnection()
                ScreenContent.NO_MOVIES -> NoMoviesFound()
                ScreenContent.MOVIES -> SearchedMovies(state)
            }
        }
    }
}

@Composable
private fun CountriesList(
    modifier: Modifier = Modifier
) {

}

@Composable
private fun ExploreCountries(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.img_news),
            contentDescription = stringResource(R.string.country_tour_image_description)
        )
        Text(
            text = stringResource(R.string.country_tour_title),
            modifier = Modifier.padding(top = 16.dp),
            style = AppTheme.textStyle.title.medium,
            color = AppTheme.color.title
        )
        Text(
            text = stringResource(R.string.country_tour_description),
            modifier = Modifier.padding(top = 8.dp),
            style = AppTheme.textStyle.body.small,
            color = AppTheme.color.body
        )
    }
}

@Composable
private fun LoadingMovies() {

}

@Composable
private fun NoInternetConnection() {

}

@Composable
private fun NoMoviesFound() {
    NoDataContainer(
        title = stringResource(R.string.no_search_result),
        description = stringResource(R.string.no_search_result_for_country),
        imageRes = painterResource(id = R.drawable.placeholder_no_result_found),
    )
}

@Composable
private fun SearchedMovies(state: SearchByCountryUiState) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = state.movies,
            key = { movie -> movie.id }
        ) { movie ->
            MovieCard(
                movieImage = painterResource(R.drawable.bg_children_wearing_3d),
                movieType = stringResource(R.string.movie),
                movieYear = movie.productionYear,
                movieTitle = movie.name,
                movieRating = movie.rating,
            )
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

@Preview(showBackground = true)
@Composable
@ThemeAndLocalePreviews
private fun SearchByCriteriaPreview() {
    AflamiTheme {
        SearchByCountryScreenContent(
            SearchByCountryUiState(),
            {},
            screenContent = ScreenContent.COUNTRY_TOUR,
            showLoadingCountries = false
        )
    }
}