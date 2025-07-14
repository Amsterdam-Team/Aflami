package com.example.ui.screens.letsPlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AppTheme

@Composable
fun LetsPlayScreen(
    modifier: Modifier = Modifier
) {
    LetsPlayScreenContent(modifier = modifier)
}

@Composable
private fun LetsPlayScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LetsPlay Screen",
            color = AppTheme.color.title,
            style = AppTheme.textStyle.title.large
        )
    }
}

@Preview
@Composable
private fun LetsPlayScreenPreview() {
    LetsPlayScreenContent()
}