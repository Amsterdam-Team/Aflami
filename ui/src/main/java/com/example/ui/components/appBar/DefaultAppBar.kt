package com.example.ui.components.appBar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.IconButton
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun DefaultAppBar(
    title: String = "",
    modifier: Modifier = Modifier,
    showNavigateBackButton: Boolean = true,
    firstOption: Painter? = null,
    firstOptionContentDescription: String? = null,
    lastOption: Painter? = null,
    lastOptionContentDescription: String? = null,
    containerColor: Color = Color.Unspecified,
    onFirstOptionClicked: () -> Unit = {},
    onLastOptionClicked: () -> Unit = {},
    onNavigateBackClicked: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        containerColor = containerColor,
        title =
            title.takeIf { it.isNotBlank() }?.let { text ->
                {
                    Text(
                        text = text,
                        color = AppTheme.color.title,
                        style = AppTheme.textStyle.title.large,
                    )
                }
            },
        leadingIcon =
            if (showNavigateBackButton) {
                {
                    IconButton(
                        painter = painterResource(R.drawable.ic__back_arrow),
                        tint = AppTheme.color.title,
                        contentDescription = stringResource(R.string.back_to_menue),
                        onClick = onNavigateBackClicked,
                    )
                }
            } else {
                null
            },
        middleIcon =
            firstOption?.let { painter ->
                {
                    IconButton(
                        painter = painter,
                        contentDescription = firstOptionContentDescription,
                        containerColor = AppTheme.color.primaryVariant,
                        tint = AppTheme.color.body,
                        paddingValues = PaddingValues(8.dp),
                        withBorder = true,
                        onClick = onFirstOptionClicked,
                    )
                }
            },
        trailingIcon =
            lastOption?.let { painter ->
                {
                    IconButton(
                        painter = painter,
                        contentDescription = lastOptionContentDescription,
                        containerColor = AppTheme.color.primaryVariant,
                        tint = AppTheme.color.body,
                        paddingValues = PaddingValues(8.dp),
                        withBorder = true,
                        onClick = onLastOptionClicked,
                    )
                }
            },
    )
}

@ThemeAndLocalePreviews
@Composable
private fun DefaultAppBarPreview() {
    AflamiTheme {
        DefaultAppBar(
            title = stringResource(R.string.my_account),
            firstOption = painterResource(R.drawable.ic_outlined_star),
            lastOption = painterResource(R.drawable.ic_sort),
            containerColor = AppTheme.color.surface,
        )
    }
}
