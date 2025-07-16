package com.example.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AppTheme

@Composable
fun LoadingContainer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(AppTheme.color.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingIndicator()
        Text(
            text = stringResource(R.string.loading),
            style = AppTheme.textStyle.label.medium,
            color = AppTheme.color.body,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}