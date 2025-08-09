package com.amsterdam.ui.screens.letsPlay.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R

@Composable
fun PlayScreenAppBar(totalScore: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = stringResource(R.string.lets_play),
            color = AppTheme.color.title,
            style = AppTheme.textStyle.title.large,
        )
        TotalScoreChip(totalScore)
    }
}