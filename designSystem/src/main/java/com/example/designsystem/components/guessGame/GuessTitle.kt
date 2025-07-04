package com.example.designsystem.components.guessGame

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun GuessTitle(
    title: String,
    points: Int,
    showHint: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    GuessCard(
        points = points,
        modifier = modifier,
        onClick = onClick,
        showHint = showHint
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center).padding(vertical = 65.dp),
            text = title,
            style = AppTheme.textStyle.title.large,
            color = AppTheme.color.title
        )
    }
}


@ThemeAndLocalePreviews
@Composable
fun GuessTitlePreview(){
    AflamiTheme {
        GuessTitle(
            title = "The Green Mile",
            points = 10,
            showHint = true,
            onClick = {}
        )
    }
}