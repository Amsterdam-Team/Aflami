package com.example.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun EmptyContainer(
    modifier: Modifier = Modifier,
    imageRes: Int,
    title: String,
    description: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 144.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            text = title,
            style = AppTheme.textStyle.body.medium,
            color = AppTheme.color.title
        )
        Text(
            text = description,
            style = AppTheme.textStyle.body.small,
            color = AppTheme.color.body
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun EmptyContainerPreview() {
    AflamiTheme {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,){
            EmptyContainer(
                imageRes = R.drawable.placeholder_no_result_found,
                title ="No search result",
                description = "please try with another keyword."
            )
        }
    }
}