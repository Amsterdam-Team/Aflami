package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.appBar.HomeAppBar
import com.example.designsystem.theme.AppTheme
import com.example.ui.application.LocalNavController
import com.example.ui.navigation.Route

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current
    HomeScreenContent(
        modifier = modifier,
        onSearchClicked = { navController.navigate(Route.Search) }
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onSearchClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .background(AppTheme.color.surface),
    ) {
        HomeAppBar(
            onSearchClicked = onSearchClicked
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreenContent(
        onSearchClicked = {}
    )
}