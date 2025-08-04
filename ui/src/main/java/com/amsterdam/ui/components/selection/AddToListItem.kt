package com.amsterdam.ui.components.selection

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
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.Text

@Composable
fun AddToListItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val backgroundColor = if (isSelected) AppTheme.color.primaryVariant else AppTheme.color.surface
    val icon =
        if (isSelected) painterResource(R.drawable.ic_checkmark_circle) else painterResource(R.drawable.ic_add_circle)
    val iconColor = if (isSelected) AppTheme.color.primary else AppTheme.color.body
    val borderColor = if (isSelected) Color.Unspecified else AppTheme.color.stroke
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
            .clickable(onClick = onClick)
            .padding(vertical = 7.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                color = AppTheme.color.body,
                style = AppTheme.textStyle.label.large,
            )
            Text(
                text = subtitle,
                color = AppTheme.color.hint,
                style = AppTheme.textStyle.label.small,
            )
        }
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconColor
        )
    }
}

@Composable
@ThemeAndLocalePreviews
private fun AddToListItemPreview() {
    AflamiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AddToListItem(
                title = stringResource(R.string.lists),
                subtitle = stringResource(R.string.lists),
                isSelected = true,
            )
            AddToListItem(
                title = stringResource(R.string.lists),
                subtitle = stringResource(R.string.lists),
                isSelected = false,
            )
        }
    }
}