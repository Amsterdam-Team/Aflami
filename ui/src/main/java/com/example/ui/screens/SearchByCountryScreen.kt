package com.example.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AflamiTheme
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
    HandleUiEffects(viewModel)
    SearchByCountryScreenContent(state, viewModel::onCountryNameUpdated, modifier)
}

@Composable
private fun HandleUiEffects(viewModel: SearchByCountryViewModel) {
    LaunchedEffect(
        viewModel.effect
    ) {
        viewModel.effect.collectLatest {
            when (it) {
                SearchByCountryEffect.NoInternetConnectionEffect -> TODO()
                SearchByCountryEffect.LoadingMoviesEffect -> TODO()
                SearchByCountryEffect.LoadingSuggestedCountriesEffect -> TODO()
                SearchByCountryEffect.MoviesLoadedEffect -> TODO()
                SearchByCountryEffect.NoMoviesEffect -> TODO()
                SearchByCountryEffect.NoSuggestedCountriesEffect -> TODO()
                SearchByCountryEffect.SuggestedCountriesLoadedEffect -> TODO()
                SearchByCountryEffect.InitialEffect -> TODO()
                SearchByCountryEffect.CountryTooShortEffect -> TODO()
            }
        }
    }
}

@Composable
fun SearchByCountryScreenContent(
    state: SearchByCountryUiState,
    onCountryNameUpdated: (String) -> Unit,
    modifier: Modifier = Modifier
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
    }
}

@Preview(showBackground = true)
@Composable
@ThemeAndLocalePreviews
private fun SearchByCriteriaPreview() {
    AflamiTheme {
        SearchByCountryScreenContent(
            SearchByCountryUiState(),
            {}
        )
    }
}