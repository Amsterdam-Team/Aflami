package com.example.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.designsystem.components.buttons.PrimaryButton
import com.example.designsystem.theme.AppTheme
import com.example.ui.application.LocalNavController
import com.example.ui.navigation.Route

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current
    SearchScreenContent(
        modifier = modifier,
        onSearchByActorClicked = { navController.navigate(Route.SearchByActor) },
        onSearchByCountryClicked = { navController.navigate(Route.SearchByCountry) },
    )
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    onSearchByActorClicked: () -> Unit,
    onSearchByCountryClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            PrimaryButton(
                modifier = Modifier.weight(1f),
                title = "Country",
                onClick = onSearchByCountryClicked,
                isEnabled = true,
                isLoading = false,
                isNegative = false
            )
            PrimaryButton(
                modifier = Modifier.weight(1f),
                title = "Actor",
                onClick = onSearchByActorClicked,
                isEnabled = true,
                isLoading = false,
                isNegative = false
            )
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreenContent(
        onSearchByActorClicked = {},
        onSearchByCountryClicked = {}
    )
}