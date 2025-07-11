package com.example.ui.screens.lists

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
fun ListsScreen(
    modifier: Modifier = Modifier
){
    ListsScreenContent(modifier = modifier)
}

@Composable
private fun ListsScreenContent(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize().background(AppTheme.color.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "List Screen",
            color = AppTheme.color.title,
            style = AppTheme.textStyle.title.large
        )
    }
}

@Preview
@Composable
private fun ListsScreenPreview(){
    ListsScreenContent()
}