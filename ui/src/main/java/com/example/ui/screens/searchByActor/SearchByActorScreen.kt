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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.application.LocalNavController

@Composable
fun SearchByActorScreen(
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current
    SearchByActorContent(
        modifier = modifier,
        onNavigateBackClicked = {
            navController.popBackStack()
        }
    )
}

@Composable
private fun SearchByActorContent(
    modifier: Modifier = Modifier,
    onNavigateBackClicked: () -> Unit = {},
    onMovieClicked: () -> Unit = {},
    onValueChange: (String) -> Unit = {}
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(30) {
                MovieCard(
                    movieImage = painterResource(R.drawable.bg_children_wearing_3d),
                    movieType = "TV show",
                    movieYear = "2016",
                    movieTitle = "Your Name",
                    movieRating = "9.9",
                    onClick = { onMovieClicked() }
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
private fun SearchByActorContentPreview() {
    AflamiTheme {
        SearchByActorContent()
    }
}