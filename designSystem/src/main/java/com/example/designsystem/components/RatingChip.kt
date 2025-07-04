package com.example.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun RatingChip(rating: String, modifier: Modifier = Modifier) {
    val roundedShape =
        RoundedCornerShape(topStart = 4.dp, topEnd = 12.dp, bottomEnd = 4.dp, bottomStart = 12.dp)
    Row(
        modifier = modifier
            .background(color = AppTheme.color.primaryVariant
                , shape = roundedShape)
            .border(width = 1.dp, color = AppTheme.color.stroke, shape = roundedShape)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_filled_star), contentDescription = null,
            modifier = Modifier
                .size(16.dp)
                .padding(end = 2.dp),
            tint = AppTheme.color.yellowAccent
        )
        Text(rating, style = AppTheme.textStyle.label.small, color = AppTheme.color.body)
    }
}

@ThemeAndLocalePreviews
@Composable
private fun RatingChipPreview() {
    AflamiTheme {
    RatingChip("9.9")
    }
}