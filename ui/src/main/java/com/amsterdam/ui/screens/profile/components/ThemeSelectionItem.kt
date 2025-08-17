package com.amsterdam.ui.screens.profile.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.buttons.RadioButton
import com.amsterdam.designsystem.components.buttons.RadioState
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R

@Composable
fun ThemeSelectionItem(
    @DrawableRes trailingIcon: Int,
    modifier: Modifier = Modifier,
    text: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val backgroundColor = animateColorAsState(
            if (isSelected) AppTheme.color.primaryVariant else AppTheme.color.surface
        )
    val iconColor = animateColorAsState(
        if (isSelected) AppTheme.color.primary else AppTheme.color.body
    )
    val borderColor = animateColorAsState(
        if (isSelected) Color.Unspecified else AppTheme.color.stroke
    )
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor.value,
                shape = shape,
            )
            .border(
                color = borderColor.value,
                width = 1.dp,
                shape = shape,
            )
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = trailingIcon),
            contentDescription = null,
            tint = iconColor.value
        )
        Text(
            text = text,
            color = AppTheme.color.body,
            style = AppTheme.textStyle.label.large,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        RadioButton(
            state = if (isSelected) RadioState.Selected else RadioState.Default,
            onClick = onClick,
            modifier = Modifier,
        )
    }
}

@Composable
@ThemeAndLocalePreviews
private fun SelectionFieldPreview() {
    AflamiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ThemeSelectionItem(
                text = stringResource(R.string.light),
                isSelected = true,
                trailingIcon = com.amsterdam.designsystem.R.drawable.ic_moon
            )
            ThemeSelectionItem(
                text = stringResource(R.string.dark),
                isSelected = false,
                trailingIcon = com.amsterdam.designsystem.R.drawable.ic_sun
            )
        }
    }
}