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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.designsystem.R
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.NoDataContainer
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.viewmodel.search.countrySearch.MovieUiState
import com.example.viewmodel.searchByActor.SearchByActorEffect
import com.example.viewmodel.searchByActor.SearchByActorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchByActorScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchByActorViewModel=koinViewModel(),
    navController: NavController
){
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                SearchByActorEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                SearchByActorEffect.NoInternetConnection -> TODO()
                null -> TODO()
            }
        }
    }
    SearchByActorContent (
        modifier=modifier,
        onNavigateBackClicked = {viewModel.onBackClicked()},
        onValueChange = {viewModel.onQueryChange(it)}
    )
    if (uiState.value.movies.isEmpty()) {
        NoDataContainer(
            imageRes = painterResource(R.drawable.placeholder_no_result_found),
            title = stringResource(R.string.no_search_result),
            description = stringResource(R.string.no_search_result_description),
        )
    }
    uiState.value.movies

}

@Composable
fun SearchByActorContent(
    modifier: Modifier = Modifier,
    onNavigateBackClicked: () -> Unit ,
    onValueChange: (String) -> Unit ,
    result: List<MovieUiState> = emptyList()
) {
        DefaultAppBar(
            title = stringResource(R.string.find_by_actor),
            showNavigateBackButton = true,
            onNavigateBackClicked = {onNavigateBackClicked()}
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            TextField(
                modifier= Modifier.padding(horizontal = 16.dp),
                text="",
                hintText = stringResource(R.string.find_by_actor),
                onValueChange = {onValueChange(it)},

            )
            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = 16.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(result) {movie->
                    MovieCard(
                        movieImage = "",
                        movieType = "Movies",
                        movieYear = movie.productionYear.toString(),
                        movieTitle = movie.name,
                        movieRating = movie.rating.toString(),
                    )
                }
            }
        }
    }


@Composable
@ThemeAndLocalePreviews
private fun SearchByActorContentPreview() {
    AflamiTheme {
        SearchByActorContent(
            onNavigateBackClicked = {},
            onValueChange = {}
        )
    }
}