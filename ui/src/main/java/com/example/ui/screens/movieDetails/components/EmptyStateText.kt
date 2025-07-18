package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AppTheme

@Composable
fun EmptyStateText(text: String) {
    Text(
        text = text, modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = AppTheme.textStyle.label.large,
        color = AppTheme.color.body
    )
}
