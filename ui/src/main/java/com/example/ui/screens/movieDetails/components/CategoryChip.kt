package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AppTheme

@Composable
fun CategoryChip(modifier: Modifier = Modifier, categoryName: String) {
    Row(
        modifier = modifier
            .background(
                color = AppTheme.color.surfaceHigh,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            categoryName,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.primary
        )
    }
}