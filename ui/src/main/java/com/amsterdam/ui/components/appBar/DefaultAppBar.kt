package com.amsterdam.ui.components.appBar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.TopAppBar
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R

@Composable
fun DefaultAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    showNavigateBackButton: Boolean = true,
    firstOption: Painter? = null,
    firstOptionIconTint: Color = AppTheme.color.body,
    lastOptionIconTint: Color = AppTheme.color.body,
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
        leadingIcon =
            if (showNavigateBackButton) {
                {
                    IconButton(
                        painter = painterResource(com.amsterdam.designsystem.R.drawable.ic__back_arrow),
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
                        tint = firstOptionIconTint,
                        paddingValues = PaddingValues(8.dp),
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
                        tint = lastOptionIconTint,
                        paddingValues = PaddingValues(8.dp),
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
            firstOption = painterResource(com.amsterdam.designsystem.R.drawable.ic_outlined_star),
            lastOption = painterResource(com.amsterdam.designsystem.R.drawable.ic_outlined_add_to_favourite),
            containerColor = AppTheme.color.surface,
        )
    }
}
