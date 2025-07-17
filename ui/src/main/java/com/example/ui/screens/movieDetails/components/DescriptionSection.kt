package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R
import com.example.designsystem.components.ExpandableText
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AppTheme

@Composable
fun DescriptionSection(modifier: Modifier = Modifier, description: String) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.description),
            style = AppTheme.textStyle.headline.small,
            color = AppTheme.color.title,
        )
        ExpandableText(text = description)
    }
}
