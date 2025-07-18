package com.example.ui.components

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.R

@Composable
fun NoDataContainer(
    imageRes: Painter,
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = imageRes,
            contentDescription = null,
        )
        if (title.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                text = title,
                style = AppTheme.textStyle.title.medium,
                color = AppTheme.color.title,
            )
        }
        if (description.isNotEmpty()) {
            Text(
                text = description,
                style = AppTheme.textStyle.body.small,
                color = AppTheme.color.body,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun NoDataContainerPreview() {
    AflamiTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            NoDataContainer(
                imageRes = painterResource(id = R.drawable.placeholder_no_result_found),
                title = stringResource(R.string.no_search_result),
                description = stringResource(R.string.no_search_result_description),
            )
        }
    }
}
