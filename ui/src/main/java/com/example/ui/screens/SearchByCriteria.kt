package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
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
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun SearchByCriteria(
    modifier: Modifier = Modifier,
) {
    Column {
        DefaultAppBar(
            title = "World Tour",
            showNavigateBackButton = true,
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            TextField(
                "",
                hintText = stringResource(R.string.country_name_hint),
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
        SearchByCriteria()
    }
}