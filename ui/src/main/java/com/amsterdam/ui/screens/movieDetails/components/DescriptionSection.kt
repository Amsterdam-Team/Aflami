package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.ExpandableText
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun DescriptionSection(modifier: Modifier = Modifier, description: String) {
    AnimatedVisibility(
        visible = description.isNotEmpty(),
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Column(modifier = modifier) {
            Text(
                text = stringResource(R.string.description),
                style = AppTheme.textStyle.headline.small,
                color = AppTheme.color.title,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ExpandableText(text = description)
        }
    }
}
