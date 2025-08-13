package com.amsterdam.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.TopAppBar
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.guessGame.TimerComponent
import com.amsterdam.viewmodel.sharedGame.TimerUiState

@Composable
internal fun GameTopBar(
    title: String,
    timerUiState: TimerUiState,
    modifier: Modifier = Modifier,
    onCancelGameClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        containerColor = Color.Transparent,
        title = {
            Text(
                text = title,
                color = AppTheme.color.title,
                style = AppTheme.textStyle.title.large,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingIcon = {
            IconButton(
                painter = painterResource(R.drawable.ic_cancel),
                tint = AppTheme.color.title,
                contentDescription = stringResource(R.string.back_to_menue),
                onClick = onCancelGameClick,
            )
        },
        trailingIcon = {
            TimerComponent(timerUiState)
        },
    )
}