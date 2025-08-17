package com.amsterdam.ui.screens.profile.components

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.RadioButton
import com.amsterdam.designsystem.components.buttons.RadioState
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.screens.profile.model.Language

@Composable
fun LanguageSelectionItem(
    modifier: Modifier = Modifier,
    trailingText: String = "",
    text: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) AppTheme.color.primaryVariant else AppTheme.color.surface
    )

    val borderColor by animateColorAsState(
        if (isSelected) Color.Unspecified else AppTheme.color.stroke
    )
    val trailerTextColor by animateColorAsState(
        if (isSelected) AppTheme.color.primary else AppTheme.color.body
    )

    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = shape,
            )
            .border(
                color = borderColor,
                width = 1.dp,
                shape = shape,
            )
            .clip(shape)
            .clickable(onClick = {
                Log.d("LanguageSelectionItem", "onClick")
                onClick()
            })
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = trailingText,
            color = trailerTextColor,
            style = AppTheme.textStyle.label.large,
            modifier = Modifier
                .padding(start = 12.dp),
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
            state = if (isSelected) RadioState.Selected else RadioState.Unselected,
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
            LanguageSelectionItem(
                modifier = Modifier.padding(top = 24.dp),
                isSelected = true,
                onClick = { },
                text = stringResource(Language.ENGLISH.nameResourceId),
                trailingText = stringResource(Language.ENGLISH.exampleResourceId)
            )
            LanguageSelectionItem(
                modifier = Modifier.padding(top = 24.dp),
                isSelected = true,
                onClick = { },
                text = stringResource(Language.ARABIC.nameResourceId),
                trailingText = stringResource(Language.ARABIC.exampleResourceId)
            )
        }
    }
}