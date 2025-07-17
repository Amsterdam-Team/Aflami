package com.example.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.buttons.OutlinedButton
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun NoNetworkContainer(
    onClickRetry: () -> Unit,
    modifier: Modifier = Modifier,
    imageRes: Painter = painterResource(R.drawable.placeholder_no_connection),
    title: String = stringResource(R.string.offline_message),
    description: String = stringResource(R.string.offline_description),
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = imageRes,
            contentDescription = stringResource(R.string.offline_message),
        )
        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            text = title,
            style = AppTheme.textStyle.title.medium,
            color = AppTheme.color.title,
        )

        Text(
            text = description,
            style = AppTheme.textStyle.body.small,
            color = AppTheme.color.body,
        )
        OutlinedButton(
            title = stringResource(R.string.retry),
            onClick = onClickRetry,
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
@ThemeAndLocalePreviews
private fun NoNetworkContainerPreview() {
    AflamiTheme {
        NoNetworkContainer(
            onClickRetry = {},
        )
    }
}
