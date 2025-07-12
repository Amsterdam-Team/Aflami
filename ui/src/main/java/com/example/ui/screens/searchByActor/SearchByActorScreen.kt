package com.example.ui.screens.searchByActor


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.R
import com.example.designsystem.components.LoadingIndicator
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.NoDataContainer
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.application.LocalNavController
import com.example.viewmodel.searchByActor.SearchByActorUiState
import com.example.viewmodel.searchByActor.SearchByActorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchByActorScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchByActorViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    SearchByActorContent(
        modifier = modifier,
        onNavigateBackClicked = {
            navController.popBackStack()
        },
        onValueChange = {
            viewModel.onQueryChange(it)
        },
        state = uiState
    )
}

@Composable
private fun SearchByActorContent(
    modifier: Modifier = Modifier,
    onNavigateBackClicked: () -> Unit,
    onValueChange: (String) -> Unit,
    state: SearchByActorUiState
) {

    Column(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        DefaultAppBar(
            title = stringResource(R.string.find_by_actor),
            showNavigateBackButton = true,
            onNavigateBackClicked = { onNavigateBackClicked() }
        )
        TextField(
            text = "",
            hintText = "Tom hanks",
            onValueChange = { onValueChange(it) },
        )
        if (state.isLoading) {
            Spacer(modifier = Modifier.weight(1f))
            LoadingIndicator()
            Spacer(modifier = Modifier.weight(1f))
        } else {
            if (state.movies.isEmpty()) {
                NoDataContainer(
                    imageRes = painterResource(R.drawable.placeholder_no_result_found),
                    title = stringResource(R.string.no_search_result),
                    description = stringResource(R.string.no_search_result_description)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.movies) { movie ->
                        MovieCard(
                            movieImage = movie.poster,
                            movieType = stringResource(R.string.movies),
                            movieYear = movie.productionYear.toString(),
                            movieTitle = movie.name,
                            movieRating = movie.rating.toString(),
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.navigationBarsPadding())
                    }
                }
            }
        }
    }
}


@Composable
@ThemeAndLocalePreviews
private fun SearchByActorContentPreview() {
    AflamiTheme {
        SearchByActorScreen()
    }
}