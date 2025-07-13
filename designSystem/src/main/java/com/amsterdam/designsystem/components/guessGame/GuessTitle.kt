package com.amsterdam.designsystem.components.guessGame

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

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
private fun GuessTitleHintPreview(){
    AflamiTheme {
        GuessTitle(
            title = "The Green Mile",
            points = 10,
            showHint = true,
            onClick = {}
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GuessTitleNoHintPreview(){
    AflamiTheme {
        GuessTitle(
            title = "The Green Mile",
            points = 10,
            showHint = false,
            onClick = {}
        )
    }
}