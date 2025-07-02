package com.example.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun GenreChip(
    genre: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {

    val boxColor = if (selected) AppTheme.color.primary else AppTheme.color.surfaceHigh
    val textColor = if (selected) AppTheme.color.onPrimary else AppTheme.color.primary

    Box(
        modifier = Modifier
            .background(
                color = boxColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre,
            style = AppTheme.textStyle.label.small.copy(
                color = textColor
            ),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GenreChipPreview() {
    AflamiTheme {
        GenreChip("Drama", selected = true)
    }
}